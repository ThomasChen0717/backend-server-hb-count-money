package logic.server.service.impl;

import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSONObject;
import com.iohao.game.action.skeleton.core.exception.MsgException;
import com.iohao.game.bolt.broker.client.kit.ExternalCommunicationKit;
import com.iohao.game.bolt.broker.client.kit.UserIdSettingKit;
import common.pb.enums.ErrorCodeEnum;
import common.pb.enums.LoginPlatformEnum;
import common.pb.pb.LoginReqPb;
import common.pb.pb.LoginResPb;
import logic.server.config.NacosConfiguration;
import logic.server.dto.UserDTO;
import logic.server.parent.action.skeleton.core.flow.MyFlowContext;
import logic.server.service.ILoginService;
import logic.server.service.IUserService;
import logic.server.util.HttpUtil;
import logic.server.util.redisson.IDistributedLock;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @author mark
 * @date 2023-04-09
 */
@Slf4j
@Service
public class LoginService implements ILoginService {
    @Autowired
    private NacosConfiguration nacosConfiguration;
    @Autowired
    private IUserService userService;

    @Autowired
    private IDistributedLock distributedLock;

    public void Test(){

    }
    public void consume(){
        // 需要调用的函数
        try {
            String keyTest = "keyTest";
            distributedLock.tryLockAndExecute(keyTest, 5, -1, TimeUnit.SECONDS, () -> {
                log.info("LoginService::consume:线程 = {},拿到锁了",Thread.currentThread().getName());
                try {
                    Thread.sleep(20000L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                log.info("LoginService::consume:线程 = {},处理完毕",Thread.currentThread().getName());
            });
        } catch (InterruptedException e) {
            log.info("LoginService::consume:线程 = {},获取锁等待失败",Thread.currentThread().getName());
        }
    }

    @Override
    public LoginResPb Login(LoginReqPb loginReqPb, MyFlowContext myFlowContext) throws MsgException {
        // isForcedOffline：true 顶号流程 false 不能重复登录 （默认不能重复登录）
        boolean isForcedOffline = false;

        // 不同平台登录处理
        UserDTO userDTO = null;
        if(loginReqPb.getLoginPlatform().compareTo(LoginPlatformEnum.Dy.getName()) == 0){
            userDTO = dyLogin(loginReqPb);
        }
        ErrorCodeEnum.addOrGetUserFailed.assertNonNull(userDTO);
        // 获取用户数据后处理逻辑
        long userId = userDTO.getId();
        if (!isForcedOffline) {
            // 查询用户是否在线
            boolean existUser = ExternalCommunicationKit.existUser(userId);
            log.info("LoginService::Login:userId = {},existUser = {}", userId,existUser);
            // 如果账号在线，就抛异常 （断言 + 异常机制）
            ErrorCodeEnum.accountOnline.assertTrueThrows(existUser);
        } else {
            log.info("LoginService::Login:userId = {},进入顶号流程", userId);
            // （相当于顶号），强制断开之前的客户端连接，并让本次登录成功。
            ExternalCommunicationKit.forcedOffline(userId);
        }

        // channel 中设置用户的真实 userId；
        boolean success = UserIdSettingKit.settingUserId(myFlowContext, userId);

        // 失败抛异常码 （断言 + 异常机制）
        ErrorCodeEnum.loginError.assertTrue(success);

        LoginResPb loginResPb = new LoginResPb();
        loginResPb.setUserId(userDTO.getId()).setToken(userDTO.getToken());
        return loginResPb;
    }

    /**
     * 抖音登录
     */
    private UserDTO dyLogin(LoginReqPb loginReqPb){
        UserDTO userDTO = null;
        if(loginReqPb.getCode() != null){
            // 授权登录：通过code获取unionId
            String url = String.format("%s/jscode2session?appid=%s&secret=%s&code=%s",
                    nacosConfiguration.getDyUrl(), nacosConfiguration.getDyAppId(), nacosConfiguration.getDySecret(), loginReqPb.getCode());
            log.info("LoginService::dyLogin:url = {}",url);
            String stringRes = HttpUtil.get(url);
            log.info("LoginService::dyLogin:stringRes = {}",stringRes);
            JSONObject jsonRes = JSONObject.parseObject(stringRes);
            String unionId = jsonRes.getString("unionid");
            if (jsonRes != null && unionId != null) {
                userDTO = userService.getUserByUnionId(unionId);
                String newToken = createToken();
                if(userDTO == null){
                    // 创建新用户
                    userDTO = new UserDTO();
                    String name = LoginPlatformEnum.Dy.getName() + unionId.substring(unionId.length() - 6);
                    userDTO.setName(name).setLoginPlatform(LoginPlatformEnum.Dy.getName()).setToken(newToken).
                            setUnionId(unionId).setOpenid(jsonRes.getString("openid"));
                    userService.addUser(userDTO);
                }else{
                    // 刷新token（非必要）
                    userDTO.setToken(newToken);
                    userService.updateUser(userDTO);
                }
            }
        }else if(loginReqPb.getToken() != null){
            // 通过token获取用户
            userDTO = userService.getUserByToken(loginReqPb.getToken());
        }
        return userDTO;
    }

    private String createToken() {
        return UUID.randomUUID().toString();
    }
}
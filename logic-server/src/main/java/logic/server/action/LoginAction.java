package logic.server.action;

import com.github.javafaker.Faker;
import com.iohao.game.action.skeleton.core.exception.MsgException;
import com.iohao.game.action.skeleton.core.flow.FlowContext;
import com.iohao.game.bolt.broker.client.kit.ExternalCommunicationKit;
import com.iohao.game.bolt.broker.client.kit.UserIdSettingKit;
import common.pb.ErrorCodeEnum;
import common.pb.pb.LoginVerifyPb;
import common.pb.pb.UserInfoPb;
import logic.server.service.LoginService;
import common.pb.cmd.LoginCmdModule;
import common.pb.pb.LogicRequestPb;
import logic.server.parent.action.skeleton.core.flow.MyFlowContext;
import com.iohao.game.action.skeleton.annotation.ActionController;
import com.iohao.game.action.skeleton.annotation.ActionMethod;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Locale;

/**
 *
 * @author mark
 * @date 2023-04-09
 */
@Slf4j
@Component
@ActionController(LoginCmdModule.cmd)
public class LoginAction {
    /** 测试神器，假数据 */
    static Faker faker = new Faker(Locale.CHINA);
    @Autowired
    LoginService loginService;

    /**
     * 登录业务
     *
     * @param loginVerifyPb loginVerify
     * @param flowContext flowContext
     * @return UserInfo
     * @throws MsgException e
     */
    @ActionMethod(LoginCmdModule.loginVerify)
    public UserInfoPb loginVerify(LoginVerifyPb loginVerifyPb, FlowContext flowContext) throws MsgException{
        // 登录业务码
        int loginBizCode = loginVerifyPb.loginBizCode;

        // 通过 jwt，得到用户（玩家）数据
        UserInfoPb userInfoPb = getUserInfoByJwt(loginVerifyPb.jwt);
        userInfoPb.tempInt = loginVerifyPb.age;
        userInfoPb.time = loginVerifyPb.time;

        long userId = userInfoPb.id;

        if (loginBizCode == 0) {
            // (相当于号已经在线上了，不能重复登录)
            log.info("LoginAction::号已经在线上了，不能重复登录");

            // 查询用户是否在线
            boolean existUser = ExternalCommunicationKit.existUser(userId);
            log.info("用户是否在线 : {} - userId : {}", existUser, userId);

            // 如果账号在线，就抛异常 （断言 + 异常机制）
            ErrorCodeEnum.accountOnline.assertTrueThrows(existUser);

        } else if (loginBizCode == 1) {
            log.info("顶号 userId:{}", userId);
            // （相当于顶号），强制断开之前的客户端连接，并让本次登录成功。
            ExternalCommunicationKit.forcedOffline(userId);
        }

        // channel 中设置用户的真实 userId；
        boolean success = UserIdSettingKit.settingUserId(flowContext, userId);

        // 失败抛异常码 （断言 + 异常机制）
        ErrorCodeEnum.loginError.assertTrue(success);

        return userInfoPb;
    }

    private UserInfoPb getUserInfoByJwt(String jwt) {
        // hash jwt 当作 userId，不访问 DB 了
        int userId = Math.abs(jwt.hashCode());

        UserInfoPb userInfoPb = new UserInfoPb();
        userInfoPb.id = userId;
        userInfoPb.name = faker.name().firstName();
        userInfoPb.tempInt = 273676;
        log.info("userInfo : {}", userInfoPb);

        return userInfoPb;
    }

    /**
     * 请求、响应
     *
     * @param logicRequestPb logicRequestPb
     * @return LogicRequestPb
     */
    @ActionMethod(LoginCmdModule.test)
    public LogicRequestPb here(LogicRequestPb logicRequestPb, MyFlowContext myFlowContext) {

        loginService.helloSpring();

        // 相关文档 https://www.yuque.com/iohao/game/nelwuz#UAUE4

        log.info("请求、响应 : {}", logicRequestPb);

        log.info("my flowContext : {}", myFlowContext.getClass());
        myFlowContext.hello();

        LogicRequestPb newLogicRequestPb = new LogicRequestPb();
        newLogicRequestPb.name = logicRequestPb.name + ", I'm here ";

        return newLogicRequestPb;
    }
}

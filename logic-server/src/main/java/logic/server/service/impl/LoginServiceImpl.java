package logic.server.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.iohao.game.action.skeleton.core.exception.MsgException;
import com.iohao.game.bolt.broker.client.kit.ExternalCommunicationKit;
import com.iohao.game.bolt.broker.client.kit.UserIdSettingKit;
import common.pb.enums.ErrorCodeEnum;
import common.pb.enums.LoginPlatformEnum;
import common.pb.pb.LoginReqPb;
import common.pb.pb.LoginResPb;
import logic.server.config.NacosConfiguration;
import logic.server.dto.CfgAttributeDTO;
import logic.server.dto.CfgVehicleDTO;
import logic.server.dto.UserAttributeDTO;
import logic.server.dto.UserDTO;
import logic.server.dto.UserVehicleDTO;
import logic.server.enums.AttributeEnum;
import logic.server.parent.action.skeleton.core.flow.MyFlowContext;
import logic.server.service.ILoginService;
import logic.server.service.IUserService;
import logic.server.singleton.CfgManagerSingleton;
import logic.server.singleton.UserManagerSingleton;
import logic.server.util.HttpUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author mark
 * @date 2023-04-09
 */
@Slf4j
@Service
public class LoginServiceImpl implements ILoginService {
    @Autowired
    private NacosConfiguration nacosConfiguration;
    @Autowired
    private IUserService userService;
    @Autowired
    private UserManagerSingleton userManagerSingleton;
    @Autowired
    private CfgManagerSingleton cfgManagerSingleton;

    public void Test(){

    }

    @Override
    public LoginResPb Login(LoginReqPb loginReqPb, MyFlowContext myFlowContext) throws MsgException {
        // isForcedOffline：true 顶号流程 false 不能重复登录 （默认不能重复登录）
        boolean isForcedOffline = false;

        // 不同平台登录处理
        UserDTO userDTO = null;
        if(loginReqPb.getLoginPlatform().compareTo(LoginPlatformEnum.dy.getName()) == 0){
            userDTO = dyLogin(loginReqPb);
        }
        ErrorCodeEnum.addOrGetUserFailed.assertNonNull(userDTO);
        long userId = userDTO.getId();
        UserAttributeDTO userAttributeDTO = userService.getUserAttributeById(userId);
        ErrorCodeEnum.addOrGetUserFailed.assertNonNull(userAttributeDTO);
        UserVehicleDTO userVehicleDTO = userService.getUserVehicleById(userId);
        ErrorCodeEnum.addOrGetUserFailed.assertNonNull(userVehicleDTO);

        // 获取用户数据后处理逻辑
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

        // 从数据库获取的用户数据存储到内存中
        boolean isAddSuccess = userManagerSingleton.addUserDataToCache(userId,userDTO,userAttributeDTO,userVehicleDTO);
        ErrorCodeEnum.addUserDataToCacheFailed.assertTrue(isAddSuccess);

        // 填充登录报文回复数据
        return makeLoginResPb(userId);
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
            String openid = jsonRes.getString("openid");
            if (jsonRes != null && unionId != null) {
                userDTO = userService.getUserByUnionId(unionId);
                String newToken = createToken();
                if(userDTO == null){
                    userDTO = createUser(LoginPlatformEnum.dy.getName(),unionId,openid,newToken);
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

    /**
     * 创建新用户并且初始化用户数据
     * @param loginPlatform
     * @param unionId
     * @param newToken
     */
    @Transactional
    private UserDTO createUser(String loginPlatform,String unionId,String openid,String newToken){
        try{
            // t_user表插入新记录
            UserDTO newUserDTO = new UserDTO();
            String name = loginPlatform + unionId.substring(unionId.length() - 6);
            newUserDTO.setName(name).setLoginPlatform(loginPlatform).setToken(newToken).setUnionId(unionId).setOpenid(openid);
            userService.addUser(newUserDTO);

            // t_user_attribute表插入记录
            UserAttributeDTO newUserAttributeDTO = new UserAttributeDTO();
            newUserAttributeDTO.setUserId(newUserDTO.getId());
            userService.addUserAttribute(newUserAttributeDTO);

            // t_user_vehicle表插入记录
            UserVehicleDTO newUserVehicleDTO = new UserVehicleDTO();
            newUserVehicleDTO.setUserId(newUserDTO.getId());
            // 检测载具配置表是否有无条件开启的载具
            List<String> vehicleTypeList = new ArrayList<>();
            Map<Integer,CfgVehicleDTO> cfgVehicleDTOMap = cfgManagerSingleton.getCfgVehicleDTOMap();
            for(Map.Entry<Integer,CfgVehicleDTO> entry : cfgVehicleDTOMap.entrySet()){
                CfgVehicleDTO cfgVehicleDTO = entry.getValue();
                if(cfgVehicleDTO.getConditionCount() == 0){
                    vehicleTypeList.add(String.valueOf(cfgVehicleDTO.getVehicleType()));
                }
            }
            String stringVehicleTypeList = vehicleTypeList.stream().collect(Collectors.joining(","));
            newUserVehicleDTO.setVehicleList(stringVehicleTypeList);
            userService.addUserVehicle(newUserVehicleDTO);

            log.info("LoginServiceImpl::createUser:loginPlatform = {},unionId = {},userId = {},创建新用户成功",loginPlatform,unionId,newUserDTO.getId());
            return newUserDTO;
        }catch (Exception e){
            log.error("LoginServiceImpl::createUser:loginPlatform = {},unionId = {},message = {},创建新用户失败",loginPlatform,unionId,e.getMessage());
        }
        return null;
    }

    private LoginResPb makeLoginResPb(long userId){
        UserDTO userDTO = userManagerSingleton.getUserByIdFromCache(userId);
        UserAttributeDTO userAttributeDTO = userManagerSingleton.getUserAttributeFromCache(userId);
        UserVehicleDTO userVehicleDTO = userManagerSingleton.getUserVehicleByIdFromCache(userId);

        LoginResPb loginResPb = new LoginResPb();
        loginResPb.setUserId(userDTO.getId()).setToken(userDTO.getToken()).setMoney(userDTO.getMoney());

        int energyAddValue = Integer.valueOf((cfgManagerSingleton.getCfgGlobalByKeyFromCache("energyAddValue")).getValue()).intValue();
        int energyMaxValue = Integer.valueOf((cfgManagerSingleton.getCfgGlobalByKeyFromCache("energyMaxValue")).getValue()).intValue();
        loginResPb.setEnergyAddValue(energyAddValue).setEnergyMaxValue(energyMaxValue);

        loginResPb.setStrengthLevel(userAttributeDTO.getStrengthLevel()).setPhysicalLevel(userAttributeDTO.getPhysicalLevel())
                .setPhysicalRestoreLevel(userAttributeDTO.getPhysicalRestoreLevel()).setEnduranceLevel(userAttributeDTO.getEnduranceLevel())
                .setPetLevel(userAttributeDTO.getPetLevel()).setIncomeMultiple(userAttributeDTO.getIncomeMultiple());

        float incomeMultiple = .1f;
        loginResPb.setIncomeMultiple(incomeMultiple);

        CfgAttributeDTO cfgAttributeDTO = cfgManagerSingleton.getCfgAttributeByTypeFromCache(AttributeEnum.strengthLevel.getAttributeType());
        loginResPb.setStrengthLevelUpFormula(cfgAttributeDTO.getAttributeLevelUpFormula()).setStrengthEffectFormula(cfgAttributeDTO.getAttributeEffectFormula());
        cfgAttributeDTO = cfgManagerSingleton.getCfgAttributeByTypeFromCache(AttributeEnum.physicalLevel.getAttributeType());
        loginResPb.setPhysicalLevelUpFormula(cfgAttributeDTO.getAttributeLevelUpFormula()).setPhysicalEffectFormula(cfgAttributeDTO.getAttributeEffectFormula());
        cfgAttributeDTO = cfgManagerSingleton.getCfgAttributeByTypeFromCache(AttributeEnum.physicalRestoreLevel.getAttributeType());
        loginResPb.setPhysicalRestoreLevelUpFormula(cfgAttributeDTO.getAttributeLevelUpFormula()).setPhysicalRestoreEffectFormula(cfgAttributeDTO.getAttributeEffectFormula());
        cfgAttributeDTO = cfgManagerSingleton.getCfgAttributeByTypeFromCache(AttributeEnum.enduranceLevel.getAttributeType());
        loginResPb.setEnduranceLevelUpFormula(cfgAttributeDTO.getAttributeLevelUpFormula()).setEnduranceEffectFormula(cfgAttributeDTO.getAttributeEffectFormula());
        cfgAttributeDTO = cfgManagerSingleton.getCfgAttributeByTypeFromCache(AttributeEnum.petLevel.getAttributeType());
        loginResPb.setPetLevelUpFormula(cfgAttributeDTO.getAttributeLevelUpFormula()).setPetEffectFormula(cfgAttributeDTO.getAttributeEffectFormula());


        return loginResPb;
    }
}
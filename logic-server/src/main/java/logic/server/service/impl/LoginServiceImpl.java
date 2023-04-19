package logic.server.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.iohao.game.action.skeleton.core.exception.MsgException;
import com.iohao.game.bolt.broker.client.kit.ExternalCommunicationKit;
import com.iohao.game.bolt.broker.client.kit.UserIdSettingKit;
import common.pb.enums.ErrorCodeEnum;
import common.pb.enums.LoginPlatformEnum;
import common.pb.pb.EquipmentInfoPb;
import common.pb.pb.LoginReqPb;
import common.pb.pb.LoginResPb;
import common.pb.pb.VehicleInfoPb;
import logic.server.config.NacosConfiguration;
import logic.server.dto.CfgAttributeDTO;
import logic.server.dto.CfgEquipmentDTO;
import logic.server.dto.CfgVehicleDTO;
import logic.server.dto.UserAttributeDTO;
import logic.server.dto.UserDTO;
import logic.server.dto.UserEquipmentDTO;
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

import java.util.Map;
import java.util.UUID;

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
        Map<Integer,UserVehicleDTO> userVehicleDTOMap = userService.getUserVehicleMapById(userId);
        ErrorCodeEnum.addOrGetUserFailed.assertTrue(userVehicleDTOMap.size() > 0);
        Map<Integer,UserEquipmentDTO> userEquipmentDTOMap = userService.getUserEquipmentMapById(userId);
        ErrorCodeEnum.addOrGetUserFailed.assertTrue(userEquipmentDTOMap.size() > 0);

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
        boolean isAddSuccess = UserManagerSingleton.getInstance().addUserDataToCache(userId,userDTO,userAttributeDTO,userVehicleDTOMap,userEquipmentDTOMap);
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
            String unionId = "11111111";
            String openid = "22222222";
            //String unionId = jsonRes.getString("unionid");
            //String openid = jsonRes.getString("openid");
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
            int defaultUnlockedVehicleCount = 0;
            Map<Integer,CfgVehicleDTO> cfgVehicleDTOMap = CfgManagerSingleton.getInstance().getCfgVehicleDTOMap();
            for(Map.Entry<Integer,CfgVehicleDTO> entry : cfgVehicleDTOMap.entrySet()){
                UserVehicleDTO newUserVehicleDTO = new UserVehicleDTO();
                CfgVehicleDTO cfgVehicleDTO = entry.getValue();
                boolean isInUse = false;
                boolean isUnlocked = false;
                if(cfgVehicleDTO.getUnlockConditionCount() == 0){
                    isInUse = defaultUnlockedVehicleCount == 0 ? true : false;
                    defaultUnlockedVehicleCount++;
                    isUnlocked = true;
                }
                newUserVehicleDTO.setUserId(newUserDTO.getId()).setVehicleId(cfgVehicleDTO.getVehicleId()).setUnlocked(isUnlocked).setInUse(isInUse);
                userService.addUserVehicle(newUserVehicleDTO);
            }

            // t_user_equipment表插入记录
            Map<Integer, CfgEquipmentDTO> cfgEquipmentDTOMap = CfgManagerSingleton.getInstance().getCfgEquipmentDTOMap();
            for(Map.Entry<Integer,CfgEquipmentDTO> entryEquipment : cfgEquipmentDTOMap.entrySet()){
                UserEquipmentDTO newUserEquipment = new UserEquipmentDTO();
                boolean isUnlocked = false;
                CfgEquipmentDTO cfgEquipmentDTO = entryEquipment.getValue();
                if(cfgEquipmentDTO.getUnlockConditionCount() == 0){
                    isUnlocked = true;
                }
                newUserEquipment.setUserId(newUserDTO.getId()).setEquipmentId(cfgEquipmentDTO.getEquipmentId()).setInUse(false).setUnlocked(isUnlocked);
                userService.addUserEquipment(newUserEquipment);
            }

            log.info("LoginServiceImpl::createUser:loginPlatform = {},unionId = {},userId = {},创建新用户成功",loginPlatform,unionId,newUserDTO.getId());
            return newUserDTO;
        }catch (Exception e){
            log.error("LoginServiceImpl::createUser:loginPlatform = {},unionId = {},message = {},创建新用户失败",loginPlatform,unionId,e.getMessage());
        }
        return null;
    }

    private LoginResPb makeLoginResPb(long userId){
        UserDTO userDTO = UserManagerSingleton.getInstance().getUserByIdFromCache(userId);
        UserAttributeDTO userAttributeDTO = UserManagerSingleton.getInstance().getUserAttributeFromCache(userId);
        Map<Integer,UserVehicleDTO> userVehicleDTOMap = UserManagerSingleton.getInstance().getUserVehicleByIdFromCache(userId);
        Map<Integer,UserEquipmentDTO> userEquipmentDTOMap = UserManagerSingleton.getInstance().getUserEquipmentByIdFromCache(userId);

        LoginResPb loginResPb = new LoginResPb();
        /** 用户数据 **/
        loginResPb.setUserId(userDTO.getId()).setToken(userDTO.getToken()).setMoney(userDTO.getMoney()).setMoneyHistory(userDTO.getMoneyHistory());

        /** 全局配置数据 **/
        int energyAddValue = Integer.valueOf((CfgManagerSingleton.getInstance().getCfgGlobalByKeyFromCache("energyAddValue")).getValueName()).intValue();
        int energyMaxValue = Integer.valueOf((CfgManagerSingleton.getInstance().getCfgGlobalByKeyFromCache("energyMaxValue")).getValueName()).intValue();
        loginResPb.setEnergyAddValue(energyAddValue).setEnergyMaxValue(energyMaxValue);

        /** 用户属性等级数据 **/
        loginResPb.setStrengthLevel(userAttributeDTO.getStrengthLevel()).setPhysicalLevel(userAttributeDTO.getPhysicalLevel())
                .setPhysicalRestoreLevel(userAttributeDTO.getPhysicalRestoreLevel()).setEnduranceLevel(userAttributeDTO.getEnduranceLevel())
                .setPetLevel(userAttributeDTO.getPetLevel()).setIncomeMultiple(userAttributeDTO.getIncomeMultiple());
        float incomeMultiple = userAttributeDTO.getIncomeMultiple();
        loginResPb.setIncomeMultiple(incomeMultiple);

        /** 用户属性升级和效果公式 **/
        CfgAttributeDTO cfgAttributeDTO = CfgManagerSingleton.getInstance().getCfgAttributeByTypeFromCache(AttributeEnum.strengthLevel.getAttributeType());
        loginResPb.setStrengthLevelUpFormula(cfgAttributeDTO.getAttributeLevelUpFormula()).setStrengthEffectFormula(cfgAttributeDTO.getAttributeEffectFormula());
        cfgAttributeDTO = CfgManagerSingleton.getInstance().getCfgAttributeByTypeFromCache(AttributeEnum.physicalLevel.getAttributeType());
        loginResPb.setPhysicalLevelUpFormula(cfgAttributeDTO.getAttributeLevelUpFormula()).setPhysicalEffectFormula(cfgAttributeDTO.getAttributeEffectFormula());
        cfgAttributeDTO = CfgManagerSingleton.getInstance().getCfgAttributeByTypeFromCache(AttributeEnum.physicalRestoreLevel.getAttributeType());
        loginResPb.setPhysicalRestoreLevelUpFormula(cfgAttributeDTO.getAttributeLevelUpFormula()).setPhysicalRestoreEffectFormula(cfgAttributeDTO.getAttributeEffectFormula());
        cfgAttributeDTO = CfgManagerSingleton.getInstance().getCfgAttributeByTypeFromCache(AttributeEnum.enduranceLevel.getAttributeType());
        loginResPb.setEnduranceLevelUpFormula(cfgAttributeDTO.getAttributeLevelUpFormula()).setEnduranceEffectFormula(cfgAttributeDTO.getAttributeEffectFormula());
        cfgAttributeDTO = CfgManagerSingleton.getInstance().getCfgAttributeByTypeFromCache(AttributeEnum.petLevel.getAttributeType());
        loginResPb.setPetLevelUpFormula(cfgAttributeDTO.getAttributeLevelUpFormula()).setPetEffectFormula(cfgAttributeDTO.getAttributeEffectFormula());

        /** 用户载具数据 **/
        for(Map.Entry<Integer,UserVehicleDTO> entryVehicle : userVehicleDTOMap.entrySet()){
            UserVehicleDTO userVehicleDTO = entryVehicle.getValue();
            VehicleInfoPb vehicleInfoPb = new VehicleInfoPb();

            /** 载具实例数据 **/
            vehicleInfoPb.setVehicleId(userVehicleDTO.getVehicleId()).setInUse(userVehicleDTO.isInUse()).
                    setUnlockConditionCurrCount(userVehicleDTO.getUnlockConditionCurrCount()).setUnlocked(userVehicleDTO.isUnlocked());
            /** 载具配置数据 **/
            Map<Integer,CfgVehicleDTO> cfgVehicleDTOMap = CfgManagerSingleton.getInstance().getCfgVehicleDTOMap();
            CfgVehicleDTO cfgVehicleDTO = cfgVehicleDTOMap.get(userVehicleDTO.getVehicleId());
            vehicleInfoPb.setVehicleName(cfgVehicleDTO.getVehicleName()).setUnlockConditionType(cfgVehicleDTO.getUnlockConditionType())
                    .setUnlockConditionCount(cfgVehicleDTO.getUnlockConditionType()).setCapacity(cfgVehicleDTO.getVehicleCapacity())
                    .setExtraRewardValue(cfgVehicleDTO.getExtraRewardValue());

            loginResPb.getVehicleInfoPbList().add(vehicleInfoPb);
        }

        /** 用户装备数据 **/
        for(Map.Entry<Integer,UserEquipmentDTO> entryEquipment : userEquipmentDTOMap.entrySet()){
            UserEquipmentDTO userEquipmentDTO = entryEquipment.getValue();
            EquipmentInfoPb equipmentInfoPb = new EquipmentInfoPb();

            /** 装备实例数据 **/
            equipmentInfoPb.setEquipmentId(userEquipmentDTO.getEquipmentId()).setInUse(userEquipmentDTO.isInUse()).
                    setUnlockConditionCurrCount(userEquipmentDTO.getUnlockConditionCurrCount()).setUnlocked(userEquipmentDTO.isUnlocked());
            /** 载具配置数据 **/
            Map<Integer,CfgEquipmentDTO> cfgEquipmentDTOMap = CfgManagerSingleton.getInstance().getCfgEquipmentDTOMap();
            CfgEquipmentDTO cfgEquipmentDTO = cfgEquipmentDTOMap.get(userEquipmentDTO.getEquipmentId());
            equipmentInfoPb.setEquipmentName(cfgEquipmentDTO.getEquipmentName()).setUnlockConditionType(cfgEquipmentDTO.getUnlockConditionType())
                    .setUnlockConditionCount(cfgEquipmentDTO.getUnlockConditionType()).setEffectAttributeType(cfgEquipmentDTO.getEffectAttributeType())
                    .setEffectAttributeMultiple(cfgEquipmentDTO.getEffectAttributeMultiple()).setEffectAttributeRemark(cfgEquipmentDTO.getEffectAttributeRemark());

            loginResPb.getEquipmentInfoPbList().add(equipmentInfoPb);
        }
        return loginResPb;
    }
}
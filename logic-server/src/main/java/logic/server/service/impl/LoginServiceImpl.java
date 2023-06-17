package logic.server.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.iohao.game.action.skeleton.core.commumication.ProcessorContext;
import com.iohao.game.action.skeleton.core.exception.MsgException;
import com.iohao.game.action.skeleton.protocol.processor.EndPointLogicServerMessage;
import com.iohao.game.bolt.broker.client.kit.ExternalCommunicationKit;
import com.iohao.game.bolt.broker.client.kit.UserIdSettingKit;
import com.iohao.game.bolt.broker.core.client.BrokerClientHelper;
import common.pb.enums.ErrorCodeEnum;
import common.pb.enums.LoginPlatformEnum;
import common.pb.pb.BossInfoPb;
import common.pb.pb.BuffToolInfoPb;
import common.pb.pb.EffectAttributeInfoPb;
import common.pb.pb.EquipmentInfoPb;
import common.pb.pb.LoginReqPb;
import common.pb.pb.LoginResPb;
import common.pb.pb.MagnateInfoPb;
import common.pb.pb.VehicleInfoNewPb;
import common.pb.pb.VehicleInfoPb;
import common.pb.pb.VipInfoPb;
import logic.server.config.NacosConfiguration;
import logic.server.dto.CfgAttributeDTO;
import logic.server.dto.CfgBossDTO;
import logic.server.dto.CfgBuffToolDTO;
import logic.server.dto.CfgEquipmentDTO;
import logic.server.dto.CfgMagnateDTO;
import logic.server.dto.CfgVehicleDTO;
import logic.server.dto.CfgVehicleNewDTO;
import logic.server.dto.CfgVipDTO;
import logic.server.dto.UserAttributeDTO;
import logic.server.dto.UserBossDTO;
import logic.server.dto.UserBuffToolDTO;
import logic.server.dto.UserDTO;
import logic.server.dto.UserEquipmentDTO;
import logic.server.dto.UserMagnateDTO;
import logic.server.dto.UserVehicleDTO;
import logic.server.dto.UserVehicleNewDTO;
import logic.server.dto.UserVipDTO;
import logic.server.enums.AttributeEnum;
import logic.server.event.name.UserLogoutEvent;
import logic.server.event.publisher.EventPublisher;
import logic.server.parent.action.skeleton.core.flow.MyFlowContext;
import logic.server.service.ILoginService;
import logic.server.service.IUserService;
import logic.server.service.impl.action.SettlementExecutor;
import logic.server.singleton.CfgManagerSingleton;
import logic.server.singleton.UserManagerSingleton;
import logic.server.util.HttpUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
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
    private EventPublisher eventPublisher;
    @Autowired
    private SettlementExecutor settlementExecutor;

    @Override
    public LoginResPb Login(LoginReqPb loginReqPb, MyFlowContext myFlowContext) throws MsgException {
        log.info("LoginServiceImpl::Login:loginReqPb = {},start", loginReqPb);
        // isForcedOffline：true 顶号流程 false 不能重复登录 （默认不能重复登录）
        boolean isForcedOffline = false;

        // 不同平台登录处理
        UserDTO userDTO = dyLogin(loginReqPb);
        if (userDTO == null) {
            log.info("LoginServiceImpl::Login:code = {},message = {},end", ErrorCodeEnum.addOrGetUserFailed.getCode(), ErrorCodeEnum.addOrGetUserFailed.getMsg());
            return new LoginResPb().setCode(ErrorCodeEnum.addOrGetUserFailed.getCode()).setMessage(ErrorCodeEnum.addOrGetUserFailed.getMsg());
        }

        long userId = userDTO.getId();
        // 获取角色数据后处理逻辑
        if (!isForcedOffline) {
            // 查询用户是否在线
            boolean existUser = ExternalCommunicationKit.existUser(userId);
            log.info("LoginService::Login:userId = {},existUser = {}", userId, existUser);
            // 如果账号在线，就抛异常 （断言 + 异常机制）
            //ErrorCodeEnum.accountOnline.assertTrueThrows(existUser);
            if (existUser) {
                log.info("LoginServiceImpl::Login:userId = {},code = {},message = {},end", userId, ErrorCodeEnum.accountOnline.getCode(), ErrorCodeEnum.accountOnline.getMsg());
                return new LoginResPb().setCode(ErrorCodeEnum.accountOnline.getCode()).setMessage(ErrorCodeEnum.accountOnline.getMsg());
            }
        } else {
            log.info("LoginService::Login:userId = {},进入顶号流程", userId);
            // （相当于顶号），强制断开之前的客户端连接，并让本次登录成功。
            ExternalCommunicationKit.forcedOffline(userId);
        }

        /** 检测角色此逻辑服内存中是否存在数据 **/
        boolean isUserDataStillInCache = UserManagerSingleton.getInstance().getUserByIdFromCache(userId) == null ? false : true;
        if (isUserDataStillInCache) {
            log.info("LoginServiceImpl::Login:userId = {},code = {},message = {},end", userId, ErrorCodeEnum.userDataStillInCache.getCode(), ErrorCodeEnum.userDataStillInCache.getMsg());
            return new LoginResPb().setCode(ErrorCodeEnum.userDataStillInCache.getCode()).setMessage(ErrorCodeEnum.userDataStillInCache.getMsg());
        }
        /** 有多个逻辑服后应检测t_user.online_server_id是否等于0 **/
        if (true) {
            if (userDTO.getOnlineServerId() > 0) {
                log.info("LoginServiceImpl::Login:userId = {},code = {},message = {},onlineServerId = {},end", userId, ErrorCodeEnum.userStillOnline.getCode(), ErrorCodeEnum.userStillOnline.getMsg(), userDTO.getOnlineServerId());
                return new LoginResPb().setCode(ErrorCodeEnum.userStillOnline.getCode()).setMessage(ErrorCodeEnum.userStillOnline.getMsg());
            }
        }

        /** 检测数据库中是否存在角色数据 **/
        UserAttributeDTO userAttributeDTO = userService.getUserAttributeByIdFromDB(userId);
        Map<Integer, UserVehicleDTO> userVehicleDTOMap = userService.getUserVehicleMapByIdFromDB(userId);
        Map<Integer, UserVehicleNewDTO> userVehicleNewDTOMap = userService.getUserVehicleNewMapByIdFromDB(userId);
        Map<Integer, UserEquipmentDTO> userEquipmentDTOMap = userService.getUserEquipmentMapByIdFromDB(userId);
        Map<Integer, UserBuffToolDTO> userBuffToolDTOMap = userService.getUserBuffToolMapByIdFromDB(userId);
        Map<Integer, UserMagnateDTO> userMagnateDTOMap = userService.getUserMagnateMapByIdFromDB(userId);
        Map<Integer, UserBossDTO> userBossDTOMap = userService.getUserBossMapByIdFromDB(userId);
        UserVipDTO userVipDTO = userService.getUserVipByIdFromDB(userId);

        // channel 中设置用户的真实 userId；
        boolean success = UserIdSettingKit.settingUserId(myFlowContext, userId);
        //ErrorCodeEnum.loginError.assertTrue(success);
        if (!success) {
            log.info("LoginServiceImpl::Login:userId = {},code = {},message = {},end", userId, ErrorCodeEnum.loginError.getCode(), ErrorCodeEnum.loginError.getMsg());
            return new LoginResPb().setCode(ErrorCodeEnum.loginError.getCode()).setMessage(ErrorCodeEnum.loginError.getMsg());
        }

        // 用户绑定逻辑服
        boolean isBindSuccess = userBindServerId(userId);
        if (!isBindSuccess) {
            log.info("LoginServiceImpl::Login:userId = {},code = {},message = {},end", userId, ErrorCodeEnum.userBindServerIdFailed.getCode(), ErrorCodeEnum.userBindServerIdFailed.getMsg());
            return new LoginResPb().setCode(ErrorCodeEnum.userBindServerIdFailed.getCode()).setMessage(ErrorCodeEnum.userBindServerIdFailed.getMsg());
        } else {
            // 绑定成功，设置并且保存t_user.online_server_id,表示角色在线
            userDTO.setOnlineServerId(CfgManagerSingleton.getInstance().getServerId());
            userService.updateUserToDB(userDTO);
        }

        // 从数据库获取的角色数据存储到内存中
        boolean isAddSuccess = UserManagerSingleton.getInstance().addUserDataToCache(userId, userDTO, userAttributeDTO, userVehicleDTOMap, userVehicleNewDTOMap, userEquipmentDTOMap, userBuffToolDTOMap, userMagnateDTOMap, userBossDTOMap, userVipDTO);
        if (!isAddSuccess) {
            log.info("LoginServiceImpl::Login:userId = {},code = {},message = {},end", userId, ErrorCodeEnum.addUserDataToCacheFailed.getCode(), ErrorCodeEnum.addUserDataToCacheFailed.getMsg());
            return new LoginResPb().setCode(ErrorCodeEnum.addUserDataToCacheFailed.getCode()).setMessage(ErrorCodeEnum.addUserDataToCacheFailed.getMsg());
        }

        // 老用户需要检测载具,装备,buffTool,富豪挑战模版数据，是否有新增
        if (!userDTO.isNewUser()) {
            checkCfgVehicleOnOldUserLogin(userDTO.getId());
            checkCfgEquipmentOnOldUserLogin(userDTO.getId());
            checkCfgBuffToolOnOldUserLogin(userDTO.getId());
            checkCfgMagnateOnOldUserLogin(userDTO.getId());
            checkCfgBossOnOldUserLogin(userDTO.getId());
            checkCfgVipOnOldUserLogin(userDTO.getId());
            checkCfgVehicleNewOnOldUserLogin(userDTO.getId());
        }

        // 数据修复
        {
            // 用户版本号:之前没有记录版本号的统一设置为：1.0.0
            if (userDTO.getClientVersion() == null || userDTO.getClientVersion().isEmpty()) {
                String defaultClientVersion = "1.0.0";
                userDTO.setClientVersion(defaultClientVersion);
            }
        }

        // 填充登录报文回复数据
        return makeLoginResPb(userId);
    }

    /**
     * 抖音登录
     */
    private UserDTO dyLogin(LoginReqPb loginReqPb) {
        UserDTO userDTO = null;
        boolean isNewUser = false;
        Date currTime = new Date();
        if (loginReqPb.getCode() != null) {
            // 授权登录：通过code获取unionId
            JSONObject jsonResult = getPlatformUserInfo(loginReqPb.getLoginPlatform(), loginReqPb.getCode());
            String unionId = jsonResult.getString("unionId");
            String openid = jsonResult.getString("openid");
            if (unionId != null) {
                userDTO = userService.getUserByUnionIdFromDB(unionId);
                String newToken = createToken();
                if (userDTO == null) {
                    userDTO = createUser(loginReqPb.getLoginPlatform(), unionId, openid, newToken, currTime, loginReqPb.clientVersion);
                    isNewUser = true;
                } else {
                    // 刷新token（非必要）
                    userDTO.setToken(newToken).setLatestLoginTime(currTime);
                    userDTO.setClientVersion(loginReqPb.clientVersion);
                    userService.updateUserToDB(userDTO);
                }
            }
        } else if (loginReqPb.getToken() != null) {
            // 通过token获取角色
            userDTO = userService.getUserByTokenFromDB(loginReqPb.getToken());
            userDTO.setClientVersion(loginReqPb.clientVersion);
            userDTO.setLatestLoginTime(currTime);
            userService.updateUserToDB(userDTO);
        }

        userDTO.setNewUser(isNewUser);
        return userDTO;
    }

    private JSONObject getPlatformUserInfo(String platform, String code) {
        JSONObject jsonPlatformUserInfo = new JSONObject();
        Date currTime = new Date();

        if (platform.compareTo("dy") == 0) {
            String url = String.format("%s/jscode2session?appid=%s&secret=%s&code=%s", nacosConfiguration.getDyUrl(), nacosConfiguration.getDyAppId(), nacosConfiguration.getDySecret(), code);
            log.info("LoginService::getPlatformUserInfo:url = {},platform = {}", url, platform);
            String stringRes = HttpUtil.get(url);
            log.info("LoginService::getPlatformUserInfo:stringRes = {}", stringRes);
            JSONObject jsonRes = JSONObject.parseObject(stringRes);
            String unionId = jsonRes.getString("unionid");
            String openid = jsonRes.getString("openid");
            jsonPlatformUserInfo.put("unionId", unionId);
            jsonPlatformUserInfo.put("openid", openid);
        } else if (platform.compareTo("ty") == 0) {
            String url = String.format("%s/plat/v1/account/token-info", nacosConfiguration.getTyUrl());
            log.info("LoginService::getPlatformUserInfo:url = {},platform = {}", url, platform);

            JSONObject jsonParam = new JSONObject();
            jsonParam.put("game_id", nacosConfiguration.getTyAppId());
            jsonParam.put("user_token", code);
            int timestamp = (int) (currTime.getTime() / 1000L);
            String nonce = createToken();
            jsonParam.put("timestamp", timestamp);
            jsonParam.put("nonce", nonce);
            // 签名
            String signParam = String.format("nonce=%s&secret=%s&timestamp=%d", nonce, nacosConfiguration.getTySecret(), timestamp);
            String sign = "";
            try {
                MessageDigest digest = MessageDigest.getInstance("SHA-1");
                byte[] hash = digest.digest(signParam.getBytes());
                StringBuffer hexString = new StringBuffer();
                for (int i = 0; i < hash.length; i++) {
                    String hex = Integer.toHexString(0xff & hash[i]);
                    if (hex.length() == 1) hexString.append('0');
                    hexString.append(hex);
                }
                sign = hexString.toString();
            } catch (Exception e) {
                log.error("LoginService::getPlatformUserInfo:message = {},签名失败", e.getMessage());
            }
            jsonParam.put("signature", sign);
            log.info("LoginService::getPlatformUserInfo:url = {},jsonParam = {},sign = {},signParam = {}", url, jsonParam, sign, signParam);
            String stringRes = HttpUtil.post(url, jsonParam, "json");
            log.info("LoginService::getPlatformUserInfo:stringRes = {}", stringRes);
            JSONObject jsonRes = JSONObject.parseObject(stringRes);
            JSONObject jsonData = jsonRes.getJSONObject("data");
            if (jsonData != null) {
                jsonPlatformUserInfo.put("unionId", jsonData.getString("union_id"));
                jsonPlatformUserInfo.put("openid", jsonData.getString("open_id"));
            }
        } else {
            //if(!(nacosConfiguration.getSpringProfilesActive().compareTo("prod") == 0) ){
            // 不是正式服，处理hb平台参数
            if (platform.compareTo("hb") == 0) {
                String unionId = String.valueOf(currTime.getTime());
                String openid = "22222222";
                jsonPlatformUserInfo.put("unionId", unionId);
                jsonPlatformUserInfo.put("openid", openid);
            }
            //}
        }

        return jsonPlatformUserInfo;
    }

    private String createToken() {
        return UUID.randomUUID().toString();
    }

    /**
     * 创建新角色并且初始化角色数据
     *
     * @param loginPlatform
     * @param unionId
     * @param newToken
     */
    private UserDTO createUser(String loginPlatform, String unionId, String openid, String newToken, Date currTime, String clientVersion) {
        try {
            // t_user表插入新记录
            UserDTO newUserDTO = new UserDTO();
            String name = loginPlatform + unionId.substring(unionId.length() - 6);
            int privilegeLevel = nacosConfiguration.getSpringProfilesActive().compareTo("prod") == 0 ? 0 : 1;
            newUserDTO.setName(name).setTitle(CfgManagerSingleton.getInstance().getCfgGlobalByKeyFromCache("firstTitle").getValueName()).setLoginPlatform(loginPlatform).setToken(newToken).setUnionId(unionId).setOpenid(openid).setLatestLoginTime(currTime).setLatestLogoutTime(currTime).setPrivilegeLevel(privilegeLevel).setMoney(100L).setClientVersion(clientVersion).setFirstClientVersion(clientVersion).setClickCount(1);
            userService.addUserToDB(newUserDTO);

            // t_user_attribute表插入记录
            UserAttributeDTO newUserAttributeDTO = new UserAttributeDTO();
            newUserAttributeDTO.setUserId(newUserDTO.getId());
            userService.addUserAttributeToDB(newUserAttributeDTO);

            // t_user_vehicle表插入记录
            int defaultUnlockedVehicleCount = 0;
            Map<Integer, CfgVehicleDTO> cfgVehicleDTOMap = CfgManagerSingleton.getInstance().getCfgVehicleDTOMap();
            for (Map.Entry<Integer, CfgVehicleDTO> entry : cfgVehicleDTOMap.entrySet()) {
                boolean isInUse = false;
                CfgVehicleDTO cfgVehicleDTO = entry.getValue();
                if (cfgVehicleDTO.getUnlockConditionCount() == 0) {
                    isInUse = defaultUnlockedVehicleCount == 0 ? true : false;
                    defaultUnlockedVehicleCount++;
                }
                addUserVehicle(newUserDTO.getId(), isInUse, cfgVehicleDTO);
            }

            // t_user_vehicle_new 表插入记录
            Map<Integer, CfgVehicleNewDTO> cfgVehicleNewDTOMap = CfgManagerSingleton.getInstance().getCfgVehicleNewDTOMap();
            for (Map.Entry<Integer, CfgVehicleNewDTO> entry : cfgVehicleNewDTOMap.entrySet()) {
                CfgVehicleNewDTO cfgVehicleNewDTO = entry.getValue();
                addUserVehicleNew(newUserDTO.getId(), cfgVehicleNewDTO);
            }

            // t_user_equipment表插入记录
            Map<Integer, CfgEquipmentDTO> cfgEquipmentDTOMap = CfgManagerSingleton.getInstance().getCfgEquipmentDTOMap();
            for (Map.Entry<Integer, CfgEquipmentDTO> entryEquipment : cfgEquipmentDTOMap.entrySet()) {
                CfgEquipmentDTO cfgEquipmentDTO = entryEquipment.getValue();
                addUserEquipment(newUserDTO.getId(), cfgEquipmentDTO);
            }

            // t_user_buff_tool表插入记录
            Map<Integer, CfgBuffToolDTO> cfgBuffToolDTOMap = CfgManagerSingleton.getInstance().getCfgBuffToolDTOMap();
            for (Map.Entry<Integer, CfgBuffToolDTO> entryBuffTool : cfgBuffToolDTOMap.entrySet()) {
                CfgBuffToolDTO cfgBuffToolDTO = entryBuffTool.getValue();
                addUserBuffTool(newUserDTO.getId(), cfgBuffToolDTO);
            }

            // t_user_magnate表插入记录
            Map<Integer, CfgMagnateDTO> cfgMagnateDTOMap = CfgManagerSingleton.getInstance().getCfgMagnateDTOMap();
            for (Map.Entry<Integer, CfgMagnateDTO> entryMagnate : cfgMagnateDTOMap.entrySet()) {
                CfgMagnateDTO cfgMagnateDTO = entryMagnate.getValue();
                addUserMagnate(newUserDTO.getId(), cfgMagnateDTO.getPreMagnateId() == 0 ? true : false, cfgMagnateDTO);
            }

            // t_user_boss表插入记录
            Map<Integer, CfgBossDTO> cfgBossDTOMap = CfgManagerSingleton.getInstance().getCfgBossDTOMap();
            for (Map.Entry<Integer, CfgBossDTO> entryBoss : cfgBossDTOMap.entrySet()) {
                CfgBossDTO cfgBossDTO = entryBoss.getValue();
                addUserBoss(newUserDTO.getId(), cfgBossDTO.getPreBossId() == 0 ? true : false, cfgBossDTO);
            }

            // t_user_vip表插入记录
            UserVipDTO newUserVipDTO = new UserVipDTO();
            newUserVipDTO.setUserId(newUserDTO.getId()).setVipLevel(0).setVipCurrConditionCount(0);
            userService.addUserVipToDB(newUserVipDTO);

            log.info("LoginServiceImpl::createUser:loginPlatform = {},unionId = {},userId = {},创建新角色成功", loginPlatform, unionId, newUserDTO.getId());
            return newUserDTO;
        } catch (Exception e) {
            log.error("LoginServiceImpl::createUser:loginPlatform = {},unionId = {},message = {},创建新角色失败", loginPlatform, unionId, e.getMessage());
        }
        return null;
    }

    private LoginResPb makeLoginResPb(long userId) {
        UserDTO userDTO = UserManagerSingleton.getInstance().getUserByIdFromCache(userId);
        UserAttributeDTO userAttributeDTO = UserManagerSingleton.getInstance().getUserAttributeFromCache(userId);
        Map<Integer, UserVehicleDTO> userVehicleDTOMap = UserManagerSingleton.getInstance().getUserVehicleMapByIdFromCache(userId);
        Map<Integer, UserVehicleNewDTO> userVehicleNewDTOMap = UserManagerSingleton.getInstance().getUserVehicleNewMapByIdFromCache(userId);
        Map<Integer, UserEquipmentDTO> userEquipmentDTOMap = UserManagerSingleton.getInstance().getUserEquipmentMapByIdFromCache(userId);
        Map<Integer, UserBuffToolDTO> userBuffToolDTOMap = UserManagerSingleton.getInstance().getUserBuffToolMapByIdFromCache(userId);
        Map<Integer, UserMagnateDTO> userMagnateDTOMap = UserManagerSingleton.getInstance().getUserMagnateMapByIdFromCache(userId);
        Map<Integer, UserBossDTO> userBossDTOMap = UserManagerSingleton.getInstance().getUserBossMapByIdFromCache(userId);
        UserVipDTO userVipDTO = UserManagerSingleton.getInstance().getUserVipFromCache(userId);

        LoginResPb loginResPb = new LoginResPb();
        /** 角色数据 **/
        loginResPb.setUserId(userDTO.getId()).setToken(userDTO.getToken()).setMoney(userDTO.getMoney()).setMoneyHistory(userDTO.getMoneyHistory()).setTitle(userDTO.getTitle()).setPrivilegeLevel(userDTO.getPrivilegeLevel());

        /** 离线时间：单位秒 **/
        loginResPb.setOfflineTime((int) ((userDTO.getLatestLoginTime().getTime() - userDTO.getLatestLogoutTime().getTime()) / 1000));
        /** 载具（新）离线收益 **/
        long petOfflineIncome = settlementExecutor.vehicleNewIncome(userId, 1, true);
        loginResPb.setPetOfflineIncome((int) petOfflineIncome);

        /** 角色属性等级数据 **/
        loginResPb.setStrengthLevel(userAttributeDTO.getStrengthLevel()).setPhysicalLevel(userAttributeDTO.getPhysicalLevel()).setPhysicalRestoreLevel(userAttributeDTO.getPhysicalRestoreLevel()).setEnduranceLevel(userAttributeDTO.getEnduranceLevel()).setPetLevel(userAttributeDTO.getPetLevel());
        float incomeMultiple = UserManagerSingleton.getInstance().getUserIncomeMultipleAttributeFromCache(userId);
        loginResPb.setIncomeMultiple(incomeMultiple);

        /** 角色属性升级和效果公式 **/
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

        /** 全局配置数据 **/
        int energyAddValue = Integer.valueOf((CfgManagerSingleton.getInstance().getCfgGlobalByKeyFromCache("energyAddValue")).getValueName()).intValue();
        int energyMaxValue = Integer.valueOf((CfgManagerSingleton.getInstance().getCfgGlobalByKeyFromCache("energyMaxValue")).getValueName()).intValue();
        int petFinishJobTime = Integer.valueOf((CfgManagerSingleton.getInstance().getCfgGlobalByKeyFromCache("petFinishJobTime")).getValueName()).intValue();
        loginResPb.setEnergyAddValue(energyAddValue).setEnergyMaxValue(energyMaxValue).setPetFinishJobTime(petFinishJobTime);

        /** 角色载具数据 **/
        for (Map.Entry<Integer, UserVehicleDTO> entryVehicle : userVehicleDTOMap.entrySet()) {
            UserVehicleDTO userVehicleDTO = entryVehicle.getValue();
            VehicleInfoPb vehicleInfoPb = new VehicleInfoPb();

            /** 载具实例数据 **/
            vehicleInfoPb.setVehicleId(userVehicleDTO.getVehicleId()).setInUse(userVehicleDTO.isInUse()).setUnlockConditionCurrCount(userVehicleDTO.getUnlockConditionCurrCount()).setUnlocked(userVehicleDTO.isUnlocked());
            /** 载具配置数据 **/
            CfgVehicleDTO cfgVehicleDTO = CfgManagerSingleton.getInstance().getCfgVehicleByIdFromCache(userVehicleDTO.getVehicleId());
            vehicleInfoPb.setVehicleName(cfgVehicleDTO.getVehicleName()).setUnlockConditionType(cfgVehicleDTO.getUnlockConditionType()).setUnlockConditionCount(cfgVehicleDTO.getUnlockConditionCount()).setCapacity(cfgVehicleDTO.getVehicleCapacity()).setExtraRewardValue(cfgVehicleDTO.getExtraRewardValue()).setShowIndex(cfgVehicleDTO.getShowIndex()).setResourceName(cfgVehicleDTO.getResourceName());

            loginResPb.getVehicleInfoPbList().add(vehicleInfoPb);
        }
        // 载具根据showIndex排序
        List<VehicleInfoPb> vehicleInfoPbList = loginResPb.getVehicleInfoPbList();
        vehicleInfoPbList = vehicleInfoPbList.stream().sorted(Comparator.comparing(VehicleInfoPb::getShowIndex).thenComparing(VehicleInfoPb::getVehicleId)).collect(Collectors.toList());
        loginResPb.setVehicleInfoPbList(vehicleInfoPbList);

        /** 角色载具（新）数据 **/
        for (Map.Entry<Integer, UserVehicleNewDTO> entryVehicleNew : userVehicleNewDTOMap.entrySet()) {
            UserVehicleNewDTO userVehicleNewDTO = entryVehicleNew.getValue();
            VehicleInfoNewPb vehicleInfoNewPb = new VehicleInfoNewPb();

            /** 载具（新）实例数据 **/
            vehicleInfoNewPb.setVehicleId(userVehicleNewDTO.getVehicleId()).setUnlockConditionCurrCount(userVehicleNewDTO.getUnlockConditionCurrCount()).setUnlocked(userVehicleNewDTO.isUnlocked()).setLevel(userVehicleNewDTO.getLevel());
            /** 载具（新）配置数据 **/
            CfgVehicleNewDTO cfgVehicleNewDTO = CfgManagerSingleton.getInstance().getCfgVehicleNewByIdFromCache(userVehicleNewDTO.getVehicleId());
            vehicleInfoNewPb.setShowIndex(cfgVehicleNewDTO.getShowIndex());

            loginResPb.getVehicleInfoNewPbList().add(vehicleInfoNewPb);
        }
        // 载具根据showIndex排序
        List<VehicleInfoNewPb> vehicleInfoNewPbList = loginResPb.getVehicleInfoNewPbList();
        vehicleInfoNewPbList = vehicleInfoNewPbList.stream().sorted(Comparator.comparing(VehicleInfoNewPb::getShowIndex).thenComparing(VehicleInfoNewPb::getVehicleId)).collect(Collectors.toList());
        loginResPb.setVehicleInfoNewPbList(vehicleInfoNewPbList);

        /** 角色装备数据 **/
        for (Map.Entry<Integer, UserEquipmentDTO> entryEquipment : userEquipmentDTOMap.entrySet()) {
            UserEquipmentDTO userEquipmentDTO = entryEquipment.getValue();
            EquipmentInfoPb equipmentInfoPb = new EquipmentInfoPb();

            /** 装备实例数据 **/
            equipmentInfoPb.setEquipmentId(userEquipmentDTO.getEquipmentId()).setInUse(userEquipmentDTO.isInUse()).setUnlockConditionCurrCount(userEquipmentDTO.getUnlockConditionCurrCount()).setUnlocked(userEquipmentDTO.isUnlocked());

            /** 装备配置数据 **/
            CfgEquipmentDTO cfgEquipmentDTO = CfgManagerSingleton.getInstance().getCfgEquipmentByIdFromCache(userEquipmentDTO.getEquipmentId());
            equipmentInfoPb.setEquipmentName(cfgEquipmentDTO.getEquipmentName()).setUnlockConditionType(cfgEquipmentDTO.getUnlockConditionType()).setUnlockConditionCount(cfgEquipmentDTO.getUnlockConditionCount()).setEffectAttributeType(cfgEquipmentDTO.getEffectAttributeType()).setEffectAttributeMultiple(cfgEquipmentDTO.getEffectAttributeMultiple()).setEffectAttributeRemark(cfgEquipmentDTO.getEffectAttributeRemark()).setShowIndex(cfgEquipmentDTO.getShowIndex()).setPreEquipmentId(cfgEquipmentDTO.getPreEquipmentId()).setResourceName(cfgEquipmentDTO.getResourceName()).setPreConditionChallengeType(cfgEquipmentDTO.getPreConditionChallengeType()).setPreConditionChallengeId(cfgEquipmentDTO.getPreConditionChallengeId());

            loginResPb.getEquipmentInfoPbList().add(equipmentInfoPb);
        }
        // 装备根据showIndex排序
        List<EquipmentInfoPb> equipmentInfoPbList = loginResPb.getEquipmentInfoPbList();
        equipmentInfoPbList = equipmentInfoPbList.stream().sorted(Comparator.comparing(EquipmentInfoPb::getShowIndex).thenComparing(EquipmentInfoPb::getEquipmentId)).collect(Collectors.toList());
        loginResPb.setEquipmentInfoPbList(equipmentInfoPbList);

        /** 角色buffTool数据 **/
        for (Map.Entry<Integer, UserBuffToolDTO> entryBuffTool : userBuffToolDTOMap.entrySet()) {
            UserBuffToolDTO userBuffToolDTO = entryBuffTool.getValue();
            BuffToolInfoPb buffToolInfoPb = new BuffToolInfoPb();

            /** buffTool配置数据 **/
            CfgBuffToolDTO cfgBuffToolDTO = CfgManagerSingleton.getInstance().getCfgBuffToolByIdFromCache(userBuffToolDTO.getBuffToolId());
            buffToolInfoPb.setBuffToolId(cfgBuffToolDTO.getBuffToolId()).setDurations(cfgBuffToolDTO.getDurations());
            List<EffectAttributeInfoPb> effectAttributeInfoPbList = new ArrayList<>();
            JSONArray jsonArrayEffectAttributeInfo = JSONArray.parseArray(cfgBuffToolDTO.getEffectAttributeInfo());
            for (int i = 0; i < jsonArrayEffectAttributeInfo.size(); i++) {
                EffectAttributeInfoPb effectAttributeInfoPb = new EffectAttributeInfoPb();
                effectAttributeInfoPb.setAttributeType(jsonArrayEffectAttributeInfo.getJSONObject(i).getInteger("attributeType"));
                effectAttributeInfoPb.setMultiple(jsonArrayEffectAttributeInfo.getJSONObject(i).getFloat("multiple"));
                effectAttributeInfoPbList.add(effectAttributeInfoPb);
            }
            buffToolInfoPb.setEffectAttributeInfoPbList(effectAttributeInfoPbList);

            loginResPb.getBuffToolInfoPbList().add(buffToolInfoPb);
        }

        /** 角色富豪挑战数据 **/
        for (Map.Entry<Integer, UserMagnateDTO> entryMagnate : userMagnateDTOMap.entrySet()) {
            UserMagnateDTO userMagnateDTO = entryMagnate.getValue();
            MagnateInfoPb magnateInfoPb = new MagnateInfoPb();

            /** 富豪挑战实例数据 **/
            magnateInfoPb.setMagnateId(userMagnateDTO.getMagnateId()).setUnlocked(userMagnateDTO.isUnlocked()).setBeat(userMagnateDTO.isBeat());

            /** 富豪挑战配置数据 **/
            CfgMagnateDTO cfgMagnateDTO = CfgManagerSingleton.getInstance().getCfgMagnateByIdFromCache(userMagnateDTO.getMagnateId());
            magnateInfoPb.setMagnateName(cfgMagnateDTO.getMagnateName()).setSpeed(cfgMagnateDTO.getSpeed()).setTargetMoneyAmount(cfgMagnateDTO.getTargetMoneyAmount()).setRewardMoneyAmount(cfgMagnateDTO.getRewardMoneyAmount()).setUnlockVehicleId(cfgMagnateDTO.getUnlockVehicleId()).setCdTime(cfgMagnateDTO.getCdTime()).setChallengeTime(cfgMagnateDTO.getChallengeTime()).setPreMagnateId(cfgMagnateDTO.getPreMagnateId()).setResourceName(cfgMagnateDTO.getResourceName()).setBossWord(cfgMagnateDTO.getBossWord()).setFixed(cfgMagnateDTO.getFixed());

            loginResPb.getMagnateInfoPbList().add(magnateInfoPb);
        }
        // 富豪根据showIndex排序
        List<MagnateInfoPb> magnateInfoPbList = loginResPb.getMagnateInfoPbList();
        magnateInfoPbList = magnateInfoPbList.stream().sorted(Comparator.comparing(MagnateInfoPb::getShowIndex).thenComparing(MagnateInfoPb::getMagnateId)).collect(Collectors.toList());
        loginResPb.setMagnateInfoPbList(magnateInfoPbList);

        /** 角色boss挑战数据 **/
        for (Map.Entry<Integer, UserBossDTO> entryBoss : userBossDTOMap.entrySet()) {
            UserBossDTO userBossDTO = entryBoss.getValue();
            BossInfoPb bossInfoPb = new BossInfoPb();

            /** boss挑战实例数据 **/
            bossInfoPb.setBossId(userBossDTO.getBossId()).setUnlocked(userBossDTO.isUnlocked()).setBeat(userBossDTO.isBeat());

            /** boss挑战配置数据 **/
            CfgBossDTO cfgBossDTO = CfgManagerSingleton.getInstance().getCfgBossByIdFromCache(userBossDTO.getBossId());
            bossInfoPb.setBossName(cfgBossDTO.getBossName()).setSpeed(cfgBossDTO.getSpeed()).setTargetMoneyAmount(cfgBossDTO.getTargetMoneyAmount()).setRewardMoneyAmount(cfgBossDTO.getRewardMoneyAmount()).setChallengeTime(cfgBossDTO.getChallengeTime()).setPreBossId(cfgBossDTO.getPreBossId()).setShowIndex(cfgBossDTO.getShowIndex()).setResourceName(cfgBossDTO.getResourceName()).setBossWord(cfgBossDTO.getBossWord()).setFixed(cfgBossDTO.getFixed());

            loginResPb.getBossInfoPbList().add(bossInfoPb);
        }
        // boss根据showIndex排序
        List<BossInfoPb> bossInfoPbList = loginResPb.getBossInfoPbList();
        bossInfoPbList = bossInfoPbList.stream().sorted(Comparator.comparing(BossInfoPb::getShowIndex).thenComparing(BossInfoPb::getBossId)).collect(Collectors.toList());
        loginResPb.setBossInfoPbList(bossInfoPbList);

        /** 角色vip等级数据 **/
        /** 角色vip等级实例数据 **/
        loginResPb.setVipLevel(userVipDTO.getVipLevel()).setVipCurrConditionCount(userVipDTO.getVipCurrConditionCount());
        /** 角色vip等级模版数据 **/
        Map<Integer, CfgVipDTO> cfgVipDTOMap = CfgManagerSingleton.getInstance().getCfgVipDTOMap();
        for (Map.Entry<Integer, CfgVipDTO> entry : cfgVipDTOMap.entrySet()) {
            CfgVipDTO cfgVipDTO = entry.getValue();
            VipInfoPb vipInfoPb = new VipInfoPb();
            vipInfoPb.setVipLevel(cfgVipDTO.getVipLevel()).setConditionCount(cfgVipDTO.getConditionCount());
            List<EffectAttributeInfoPb> effectAttributeInfoPbList = new ArrayList<>();
            JSONArray jsonArrayEffectAttributeInfo = JSONArray.parseArray(cfgVipDTO.getEffectAttributeInfo());
            for (int i = 0; i < jsonArrayEffectAttributeInfo.size(); i++) {
                EffectAttributeInfoPb effectAttributeInfoPb = new EffectAttributeInfoPb();
                effectAttributeInfoPb.setAttributeType(jsonArrayEffectAttributeInfo.getJSONObject(i).getInteger("attributeType"));
                effectAttributeInfoPb.setMultiple(jsonArrayEffectAttributeInfo.getJSONObject(i).getFloat("multiple"));
                effectAttributeInfoPbList.add(effectAttributeInfoPb);
            }
            vipInfoPb.setEffectAttributeInfoPbList(effectAttributeInfoPbList);
            loginResPb.getVipInfoPbList().add(vipInfoPb);
        }

        /** 选择石头次数 **/
        loginResPb.setSelectStoneCount(userDTO.getSelectStoneCount());
        /** 连续点击次数 **/
        loginResPb.setClickCount(userDTO.getClickCount());
        /** 创建角色时版本号 **/
        loginResPb.setFirstClientVersion(userDTO.getFirstClientVersion());

        return loginResPb;
    }

    private void addUserVehicle(long userId, boolean isInUse, CfgVehicleDTO cfgVehicleDTO) {
        UserVehicleDTO newUserVehicleDTO = new UserVehicleDTO();
        boolean isUnlocked = cfgVehicleDTO.getUnlockConditionCount() == 0 ? true : false;
        newUserVehicleDTO.setUserId(userId).setVehicleId(cfgVehicleDTO.getVehicleId()).setUnlocked(isUnlocked).setInUse(isInUse);
        userService.addUserVehicleToDB(newUserVehicleDTO);
        // 缓存存在，则缓存数据中也新增
        UserManagerSingleton.getInstance().addUserVehicleToCache(userId, newUserVehicleDTO.getVehicleId(), newUserVehicleDTO);
    }

    private void addUserVehicleNew(long userId, CfgVehicleNewDTO cfgVehicleNewDTO) {
        UserVehicleNewDTO newUserVehicleNewDTO = new UserVehicleNewDTO();
        newUserVehicleNewDTO.setUserId(userId).setVehicleId(cfgVehicleNewDTO.getVehicleId()).setUnlocked(false).setLevel(1);
        userService.addUserVehicleNewToDB(newUserVehicleNewDTO);
        // 缓存存在，则缓存数据中也新增
        UserManagerSingleton.getInstance().addUserVehicleNewToCache(userId, newUserVehicleNewDTO.getVehicleId(), newUserVehicleNewDTO);
    }

    private void addUserEquipment(long userId, CfgEquipmentDTO cfgEquipmentDTO) {
        UserEquipmentDTO newUserEquipmentDTO = new UserEquipmentDTO();
        newUserEquipmentDTO.setUserId(userId).setEquipmentId(cfgEquipmentDTO.getEquipmentId()).setInUse(false).setUnlocked(false);
        userService.addUserEquipmentToDB(newUserEquipmentDTO);
        // 缓存存在，则缓存数据中也新增
        UserManagerSingleton.getInstance().addUserEquipmentToCache(userId, newUserEquipmentDTO.getEquipmentId(), newUserEquipmentDTO);
    }

    private void addUserBuffTool(long userId, CfgBuffToolDTO cfgBuffToolDTO) {
        UserBuffToolDTO newUserBuffToolDTO = new UserBuffToolDTO();
        newUserBuffToolDTO.setUserId(userId).setBuffToolId(cfgBuffToolDTO.getBuffToolId()).setInUse(false);
        userService.addUserBuffToolToDB(newUserBuffToolDTO);
        // 缓存存在，则缓存数据中也新增
        UserManagerSingleton.getInstance().addUserBuffToolToCache(userId, newUserBuffToolDTO.getBuffToolId(), newUserBuffToolDTO);
    }

    private void addUserMagnate(long userId, boolean isUnlocked, CfgMagnateDTO cfgMagnateDTO) {
        UserMagnateDTO newUserMagnateDTO = new UserMagnateDTO();
        newUserMagnateDTO.setUserId(userId).setMagnateId(cfgMagnateDTO.getMagnateId()).setUnlocked(isUnlocked);
        userService.addUserMagnateToDB(newUserMagnateDTO);
        // 缓存存在，则缓存数据中也新增
        UserManagerSingleton.getInstance().addUserMagnateToCache(userId, newUserMagnateDTO.getMagnateId(), newUserMagnateDTO);
    }

    private void addUserBoss(long userId, boolean isUnlocked, CfgBossDTO cfgBossDTO) {
        UserBossDTO newUserBossDTO = new UserBossDTO();
        newUserBossDTO.setUserId(userId).setBossId(cfgBossDTO.getBossId()).setUnlocked(isUnlocked);
        userService.addUserBossToDB(newUserBossDTO);
        // 缓存存在，则缓存数据中也新增
        UserManagerSingleton.getInstance().addUserBossToCache(userId, newUserBossDTO.getBossId(), newUserBossDTO);
    }

    /**
     * 老角色登录检测是否有新的载具模版数据需处理
     *
     * @param userId
     */
    private void checkCfgVehicleOnOldUserLogin(long userId) {
        List<CfgVehicleDTO> newCfgVehicleDTOList = new ArrayList<>();
        Map<Integer, CfgVehicleDTO> cfgVehicleDTOMap = CfgManagerSingleton.getInstance().getCfgVehicleDTOMap();
        for (Map.Entry<Integer, CfgVehicleDTO> entry : cfgVehicleDTOMap.entrySet()) {
            Map<Integer, UserVehicleDTO> userVehicleDTOMap = UserManagerSingleton.getInstance().getUserVehicleMapByIdFromCache(userId);
            if (userVehicleDTOMap.get(entry.getKey()) == null) {
                newCfgVehicleDTOList.add(entry.getValue());
            }
        }
        // 新增的载具模版需要添加到角色载具数据中
        for (CfgVehicleDTO cfgVehicleDTO : newCfgVehicleDTOList) {
            addUserVehicle(userId, false, cfgVehicleDTO);
        }
    }

    /**
     * 老角色登录检测是否有新的载具（新）模版数据需处理
     *
     * @param userId
     */
    private void checkCfgVehicleNewOnOldUserLogin(long userId) {
        List<CfgVehicleNewDTO> newCfgVehicleNewDTOList = new ArrayList<>();
        Map<Integer, CfgVehicleNewDTO> cfgVehicleNewDTOMap = CfgManagerSingleton.getInstance().getCfgVehicleNewDTOMap();
        for (Map.Entry<Integer, CfgVehicleNewDTO> entry : cfgVehicleNewDTOMap.entrySet()) {
            Map<Integer, UserVehicleNewDTO> userVehicleNewDTOMap = UserManagerSingleton.getInstance().getUserVehicleNewMapByIdFromCache(userId);
            if (userVehicleNewDTOMap.get(entry.getKey()) == null) {
                newCfgVehicleNewDTOList.add(entry.getValue());
            }
        }
        // 新增的载具（新）模版需要添加到角色载具数据中
        for (CfgVehicleNewDTO cfgVehicleNewDTO : newCfgVehicleNewDTOList) {
            addUserVehicleNew(userId, cfgVehicleNewDTO);
        }
    }

    /**
     * 老用户登录检测是否有新的装备模版数据需处理
     *
     * @param userId
     */
    private void checkCfgEquipmentOnOldUserLogin(long userId) {
        List<CfgEquipmentDTO> newCfgEquipmentDTOList = new ArrayList<>();
        Map<Integer, CfgEquipmentDTO> cfgEquipmentDTOMap = CfgManagerSingleton.getInstance().getCfgEquipmentDTOMap();
        for (Map.Entry<Integer, CfgEquipmentDTO> entry : cfgEquipmentDTOMap.entrySet()) {
            Map<Integer, UserEquipmentDTO> userEquipmentDTOMap = UserManagerSingleton.getInstance().getUserEquipmentMapByIdFromCache(userId);
            if (userEquipmentDTOMap.get(entry.getKey()) == null) {
                newCfgEquipmentDTOList.add(entry.getValue());
            }
        }
        // 新增的装备模版需要添加到角色装备数据中
        for (CfgEquipmentDTO cfgEquipmentDTO : newCfgEquipmentDTOList) {
            addUserEquipment(userId, cfgEquipmentDTO);
        }
    }

    /**
     * 老用户登录检测是否有新的BuffTool模版数据需处理
     *
     * @param userId
     */
    private void checkCfgBuffToolOnOldUserLogin(long userId) {
        List<CfgBuffToolDTO> newCfgBuffToolDTOList = new ArrayList<>();
        Map<Integer, CfgBuffToolDTO> cfgBuffToolDTOMap = CfgManagerSingleton.getInstance().getCfgBuffToolDTOMap();
        for (Map.Entry<Integer, CfgBuffToolDTO> entry : cfgBuffToolDTOMap.entrySet()) {
            Map<Integer, UserBuffToolDTO> userBuffToolDTOMap = UserManagerSingleton.getInstance().getUserBuffToolMapByIdFromCache(userId);
            if (userBuffToolDTOMap.get(entry.getKey()) == null) {
                newCfgBuffToolDTOList.add(entry.getValue());
            }
        }
        // 新增的buffTool模版需要添加到角色buffTool数据中
        for (CfgBuffToolDTO cfgBuffToolDTO : newCfgBuffToolDTOList) {
            addUserBuffTool(userId, cfgBuffToolDTO);
        }
    }

    /**
     * 老用户登录检测是否有新的富豪挑战模版数据需处理
     *
     * @param userId
     */
    private void checkCfgMagnateOnOldUserLogin(long userId) {
        List<CfgMagnateDTO> newCfgMagnateDTOList = new ArrayList<>();
        Map<Integer, CfgMagnateDTO> cfgMagnateDTOMap = CfgManagerSingleton.getInstance().getCfgMagnateDTOMap();
        for (Map.Entry<Integer, CfgMagnateDTO> entry : cfgMagnateDTOMap.entrySet()) {
            Map<Integer, UserMagnateDTO> userMagnateDTOMap = UserManagerSingleton.getInstance().getUserMagnateMapByIdFromCache(userId);
            if (userMagnateDTOMap.get(entry.getKey()) == null) {
                newCfgMagnateDTOList.add(entry.getValue());
            }
        }
        // 新增的富豪模版需要添加到角色富豪挑战数据中
        for (int i = 0; i < newCfgMagnateDTOList.size(); i++) {
            addUserMagnate(userId, newCfgMagnateDTOList.get(i).getPreMagnateId() == 0 ? true : false, newCfgMagnateDTOList.get(i));
        }
    }

    /**
     * 老用户登录检测是否有新的boss挑战模版数据需处理
     *
     * @param userId
     */
    private void checkCfgBossOnOldUserLogin(long userId) {
        // 已有的boss设置下is_beat字段，此字段是新增的，如不是当前最后一个解锁的boss，都可以设置is_beat = true
        Map<Integer, CfgBossDTO> cfgBossDTOMap = CfgManagerSingleton.getInstance().getCfgBossDTOMap();
        List<CfgBossDTO> cfgBossDTOList = cfgBossDTOMap.values().stream().toList();
        cfgBossDTOList = cfgBossDTOList.stream().sorted(Comparator.comparing(CfgBossDTO::getShowIndex)).collect(Collectors.toList());

        UserBossDTO lastUserBossDTO = null;
        Map<Integer, UserBossDTO> userBossDTOMap = UserManagerSingleton.getInstance().getUserBossMapByIdFromCache(userId);
        for (CfgBossDTO cfgBossDTO : cfgBossDTOList) {
            UserBossDTO userBossDTO = userBossDTOMap.get(cfgBossDTO.getBossId());
            if (userBossDTO == null) continue;
            if (userBossDTO.isBeat()) continue;
            if (userBossDTO.isUnlocked()) {
                userBossDTO.setBeat(true);
                lastUserBossDTO = userBossDTO;
            }
        }
        if (lastUserBossDTO != null) lastUserBossDTO.setBeat(false);

        // 检测是否有新的boss配置
        List<CfgBossDTO> newCfgBossDTOList = new ArrayList<>();
        for (Map.Entry<Integer, CfgBossDTO> entry : cfgBossDTOMap.entrySet()) {
            if (userBossDTOMap.get(entry.getKey()) == null) {
                newCfgBossDTOList.add(entry.getValue());
            }
        }
        // 新增的boss模版需要添加到角色boss挑战数据中
        for (int i = 0; i < newCfgBossDTOList.size(); i++) {
            addUserBoss(userId, newCfgBossDTOList.get(i).getPreBossId() == 0 ? true : false, newCfgBossDTOList.get(i));
        }
    }

    /**
     * 老用户登录检测是否有vip等级信息
     *
     * @param userId
     */
    private void checkCfgVipOnOldUserLogin(long userId) {
        UserVipDTO userVipDTO = UserManagerSingleton.getInstance().getUserVipFromCache(userId);
        if (userVipDTO == null) {
            userVipDTO = new UserVipDTO();
            userVipDTO.setVipLevel(0).setUserId(userId).setVipCurrConditionCount(0);
            userService.addUserVipToDB(userVipDTO);
            // 缓存存在，则缓存数据中也新增
            UserManagerSingleton.getInstance().addUserVipToCache(userId, userVipDTO);
        }
    }

    /**
     * 用户绑定逻辑服：此用户所有报文都会转发至绑定的逻辑服
     *
     * @param userId
     */
    private boolean userBindServerId(long userId) {
        int serverId = CfgManagerSingleton.getInstance().getServerId();
        try {
            // 添加需要绑定的用户（玩家）
            List<Long> userIdList = new ArrayList<>();
            userIdList.add(userId);
            // 绑定消息
            EndPointLogicServerMessage endPointLogicServerMessage = new EndPointLogicServerMessage()
                    // 需要绑定的玩家，示例中只取了当前请求匹配的玩家
                    .setUserList(userIdList)
                    // 需要绑定的逻辑服id
                    .setLogicServerId(String.valueOf(serverId))
                    // true 为绑定，false 为取消绑定
                    .setBinding(true);

            // 发送消息到网关
            ProcessorContext processorContext = BrokerClientHelper.getProcessorContext();
            processorContext.invokeOneway(endPointLogicServerMessage);

            log.info("LoginServiceImpl::userBindServerId:userId = {},serverId = {}用户绑定逻辑服成功", userId, serverId);
            return true;
        } catch (Exception e) {
            log.error("LoginServiceImpl::userBindServerId:userId = {},serverId = {},message = {},用户绑定逻辑服失败", userId, serverId);
            return false;
        }
    }

    @Override
    public void Logout(MyFlowContext myFlowContext) {
        long userId = myFlowContext.getUserId();

        // 经测试：此处执行保存用户数据，如短时间内用户下线较多，会造成新用户无法登录（对外服和网关服不工作，一直在等待所有用户保存完毕，可能框架问题）
        // userService.saveDataFromCacheToDB(userId);
        // 修改为抛出用户下线事件方式
        eventPublisher.publish(new UserLogoutEvent(this, userId));

        log.info("LoginServiceImpl::Logout:userId = {},用户登出", userId);
    }
}
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
import common.pb.pb.BuffToolInfoPb;
import common.pb.pb.EffectAttributeInfoPb;
import common.pb.pb.EquipmentInfoPb;
import common.pb.pb.LoginReqPb;
import common.pb.pb.LoginResPb;
import common.pb.pb.VehicleInfoPb;
import logic.server.config.NacosConfiguration;
import logic.server.dto.CfgAttributeDTO;
import logic.server.dto.CfgBuffToolDTO;
import logic.server.dto.CfgEquipmentDTO;
import logic.server.dto.CfgVehicleDTO;
import logic.server.dto.UserAttributeDTO;
import logic.server.dto.UserBuffToolDTO;
import logic.server.dto.UserDTO;
import logic.server.dto.UserEquipmentDTO;
import logic.server.dto.UserVehicleDTO;
import logic.server.enums.AttributeEnum;
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
    private SettlementExecutor settlementExecutor;

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
        // 获取角色数据后处理逻辑
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

        /** 检测角色此逻辑服内存中是否存在数据（有多个逻辑服后应检测t_user.server_id是否等于0） **/
        boolean isUserDataStillInCache = UserManagerSingleton.getInstance().getUserByIdFromCache(userId) == null ? false : true;
        ErrorCodeEnum.userDataStillInCache.assertTrue(!isUserDataStillInCache);

        /** 检测数据库中是否存在角色数据 **/
        UserAttributeDTO userAttributeDTO = userService.getUserAttributeByIdFromDB(userId);
        ErrorCodeEnum.addOrGetUserFailed.assertNonNull(userAttributeDTO);
        Map<Integer,UserVehicleDTO> userVehicleDTOMap = userService.getUserVehicleMapByIdFromDB(userId);
        ErrorCodeEnum.addOrGetUserFailed.assertTrue(userVehicleDTOMap.size() > 0);
        Map<Integer,UserEquipmentDTO> userEquipmentDTOMap = userService.getUserEquipmentMapByIdFromDB(userId);
        ErrorCodeEnum.addOrGetUserFailed.assertTrue(userEquipmentDTOMap.size() > 0);
        Map<Integer,UserBuffToolDTO> userBuffToolDTOMap = userService.getUserBuffToolMapByIdFromDB(userId);
        ErrorCodeEnum.addOrGetUserFailed.assertTrue(userBuffToolDTOMap.size() > 0);

        // channel 中设置用户的真实 userId；
        boolean success = UserIdSettingKit.settingUserId(myFlowContext, userId);
        ErrorCodeEnum.loginError.assertTrue(success);

        // 用户绑定逻辑服
        boolean isBindSuccess = userBindServerId(userId);
        ErrorCodeEnum.userBindServerIdFailed.assertTrue(isBindSuccess);

        // 从数据库获取的角色数据存储到内存中
        boolean isAddSuccess = UserManagerSingleton.getInstance().addUserDataToCache(userId,userDTO,userAttributeDTO,userVehicleDTOMap,userEquipmentDTOMap,userBuffToolDTOMap);
        ErrorCodeEnum.addUserDataToCacheFailed.assertTrue(isAddSuccess);

        // 老用户需要检测载具和装备模版数据，是否有新增
        if(!userDTO.isNewUser()){
            checkCfgVehicleOnOldUserLogin(userDTO.getId());
            checkCfgEquipmentOnOldUserLogin(userDTO.getId());
            checkCfgBuffToolOnOldUserLogin(userDTO.getId());
        }

        // 填充登录报文回复数据
        return makeLoginResPb(userId);
    }

    /**
     * 抖音登录
     */
    private UserDTO dyLogin(LoginReqPb loginReqPb){
        UserDTO userDTO = null;
        boolean isNewUser = false;
        Date currTime = new Date();
        if(loginReqPb.getCode() != null){
            // 授权登录：通过code获取unionId
            String url = String.format("%s/jscode2session?appid=%s&secret=%s&code=%s",
                    nacosConfiguration.getDyUrl(), nacosConfiguration.getDyAppId(), nacosConfiguration.getDySecret(), loginReqPb.getCode());
            log.info("LoginService::dyLogin:url = {}",url);
            String stringRes = HttpUtil.get(url);
            log.info("LoginService::dyLogin:stringRes = {}",stringRes);
            JSONObject jsonRes = JSONObject.parseObject(stringRes);
            String unionId = String.valueOf(currTime.getTime());
            String openid = "22222222";
            //String unionId = jsonRes.getString("unionid");
            //String openid = jsonRes.getString("openid");
            if (jsonRes != null && unionId != null) {
                userDTO = userService.getUserByUnionIdFromDB(unionId);
                String newToken = createToken();
                if(userDTO == null){
                    userDTO = createUser(LoginPlatformEnum.dy.getName(),unionId,openid,newToken,currTime);
                    isNewUser = true;
                }else{
                    // 刷新token（非必要）
                    userDTO.setToken(newToken).setLatestLoginTime(currTime);
                    userService.updateUserToDB(userDTO);
                }
            }
        }else if(loginReqPb.getToken() != null){
            // 通过token获取角色
            userDTO = userService.getUserByTokenFromDB(loginReqPb.getToken());
            userDTO.setLatestLoginTime(currTime);
            userService.updateUserToDB(userDTO);
        }

        userDTO.setNewUser(isNewUser);
        return userDTO;
    }

    private String createToken() {
        return UUID.randomUUID().toString();
    }

    /**
     * 创建新角色并且初始化角色数据
     * @param loginPlatform
     * @param unionId
     * @param newToken
     */
    private UserDTO createUser(String loginPlatform,String unionId,String openid,String newToken,Date currTime){
        try{
            // t_user表插入新记录
            UserDTO newUserDTO = new UserDTO();
            String name = loginPlatform + unionId.substring(unionId.length() - 6);
            newUserDTO.setName(name).setLoginPlatform(loginPlatform).setToken(newToken).setUnionId(unionId).setOpenid(openid).setLatestLoginTime(currTime)
                    .setLatestLogoutTime(currTime);
            userService.addUserToDB(newUserDTO);

            // t_user_attribute表插入记录
            UserAttributeDTO newUserAttributeDTO = new UserAttributeDTO();
            newUserAttributeDTO.setUserId(newUserDTO.getId());
            userService.addUserAttributeToDB(newUserAttributeDTO);

            // t_user_vehicle表插入记录
            int defaultUnlockedVehicleCount = 0;
            Map<Integer,CfgVehicleDTO> cfgVehicleDTOMap = CfgManagerSingleton.getInstance().getCfgVehicleDTOMap();
            for(Map.Entry<Integer,CfgVehicleDTO> entry : cfgVehicleDTOMap.entrySet()){
                boolean isInUse = false;
                CfgVehicleDTO cfgVehicleDTO = entry.getValue();
                if(cfgVehicleDTO.getUnlockConditionCount() == 0){
                    isInUse = defaultUnlockedVehicleCount == 0 ? true : false;
                    defaultUnlockedVehicleCount++;
                }
                addUserVehicle(newUserDTO.getId(),isInUse,cfgVehicleDTO);
            }

            // t_user_equipment表插入记录
            Map<Integer, CfgEquipmentDTO> cfgEquipmentDTOMap = CfgManagerSingleton.getInstance().getCfgEquipmentDTOMap();
            for(Map.Entry<Integer,CfgEquipmentDTO> entryEquipment : cfgEquipmentDTOMap.entrySet()){
                CfgEquipmentDTO cfgEquipmentDTO = entryEquipment.getValue();
                addUserEquipment(newUserDTO.getId(),cfgEquipmentDTO);
            }

            // t_user_buff_tool表插入记录
            Map<Integer, CfgBuffToolDTO> cfgBuffToolDTOMap = CfgManagerSingleton.getInstance().getCfgBuffToolDTOMap();
            for(Map.Entry<Integer,CfgBuffToolDTO> entryBuffTool : cfgBuffToolDTOMap.entrySet()){
                CfgBuffToolDTO cfgBuffToolDTO = entryBuffTool.getValue();
                addUserBuffTool(newUserDTO.getId(),cfgBuffToolDTO);
            }

            log.info("LoginServiceImpl::createUser:loginPlatform = {},unionId = {},userId = {},创建新角色成功",loginPlatform,unionId,newUserDTO.getId());
            return newUserDTO;
        }catch (Exception e){
            log.error("LoginServiceImpl::createUser:loginPlatform = {},unionId = {},message = {},创建新角色失败",loginPlatform,unionId,e.getMessage());
        }
        return null;
    }

    private LoginResPb makeLoginResPb(long userId){
        UserDTO userDTO = UserManagerSingleton.getInstance().getUserByIdFromCache(userId);
        UserAttributeDTO userAttributeDTO = UserManagerSingleton.getInstance().getUserAttributeFromCache(userId);
        Map<Integer,UserVehicleDTO> userVehicleDTOMap = UserManagerSingleton.getInstance().getUserVehicleMapByIdFromCache(userId);
        Map<Integer,UserEquipmentDTO> userEquipmentDTOMap = UserManagerSingleton.getInstance().getUserEquipmentMapByIdFromCache(userId);
        Map<Integer,UserBuffToolDTO> userBuffToolDTOMap = UserManagerSingleton.getInstance().getUserBuffToolMapByIdFromCache(userId);

        LoginResPb loginResPb = new LoginResPb();
        /** 角色数据 **/
        loginResPb.setUserId(userDTO.getId()).setToken(userDTO.getToken()).setMoney(userDTO.getMoney()).setMoneyHistory(userDTO.getMoneyHistory());

        /** 全局配置数据 **/
        int energyAddValue = Integer.valueOf((CfgManagerSingleton.getInstance().getCfgGlobalByKeyFromCache("energyAddValue")).getValueName()).intValue();
        int energyMaxValue = Integer.valueOf((CfgManagerSingleton.getInstance().getCfgGlobalByKeyFromCache("energyMaxValue")).getValueName()).intValue();
        int petFinishJobTime = Integer.valueOf((CfgManagerSingleton.getInstance().getCfgGlobalByKeyFromCache("petFinishJobTime")).getValueName()).intValue();
        loginResPb.setEnergyAddValue(energyAddValue).setEnergyMaxValue(energyMaxValue).setPetFinishJobTime(petFinishJobTime);

        /** 角色属性等级数据 **/
        loginResPb.setStrengthLevel(userAttributeDTO.getStrengthLevel()).setPhysicalLevel(userAttributeDTO.getPhysicalLevel())
                .setPhysicalRestoreLevel(userAttributeDTO.getPhysicalRestoreLevel()).setEnduranceLevel(userAttributeDTO.getEnduranceLevel())
                .setPetLevel(userAttributeDTO.getPetLevel());
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

        /** 角色载具数据 **/
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
                    .setExtraRewardValue(cfgVehicleDTO.getExtraRewardValue()).setResourceName(cfgVehicleDTO.getResourceName());

            loginResPb.getVehicleInfoPbList().add(vehicleInfoPb);
        }

        /** 角色装备数据 **/
        for(Map.Entry<Integer,UserEquipmentDTO> entryEquipment : userEquipmentDTOMap.entrySet()){
            UserEquipmentDTO userEquipmentDTO = entryEquipment.getValue();
            EquipmentInfoPb equipmentInfoPb = new EquipmentInfoPb();

            /** 装备实例数据 **/
            equipmentInfoPb.setEquipmentId(userEquipmentDTO.getEquipmentId()).setInUse(userEquipmentDTO.isInUse())
                    .setUnlockConditionCurrCount(userEquipmentDTO.getUnlockConditionCurrCount()).setUnlocked(userEquipmentDTO.isUnlocked());

            /** 装备配置数据 **/
            Map<Integer,CfgEquipmentDTO> cfgEquipmentDTOMap = CfgManagerSingleton.getInstance().getCfgEquipmentDTOMap();
            CfgEquipmentDTO cfgEquipmentDTO = cfgEquipmentDTOMap.get(userEquipmentDTO.getEquipmentId());
            equipmentInfoPb.setEquipmentName(cfgEquipmentDTO.getEquipmentName()).setUnlockConditionType(cfgEquipmentDTO.getUnlockConditionType())
                    .setUnlockConditionCount(cfgEquipmentDTO.getUnlockConditionType()).setEffectAttributeType(cfgEquipmentDTO.getEffectAttributeType())
                    .setEffectAttributeMultiple(cfgEquipmentDTO.getEffectAttributeMultiple()).setEffectAttributeRemark(cfgEquipmentDTO.getEffectAttributeRemark())
                    .setShowIndex(cfgEquipmentDTO.getShowIndex()).setPreEquipmentId(cfgEquipmentDTO.getPreEquipmentId()).setResourceName(cfgEquipmentDTO.getResourceName());

            loginResPb.getEquipmentInfoPbList().add(equipmentInfoPb);
        }
        // 装备根据showIndex排序
        List<EquipmentInfoPb> equipmentInfoPbList = loginResPb.getEquipmentInfoPbList();
        equipmentInfoPbList = equipmentInfoPbList.stream().sorted(
                Comparator.comparing(EquipmentInfoPb::getShowIndex, Comparator.reverseOrder()).thenComparing(EquipmentInfoPb::getEquipmentId)
        ).collect(Collectors.toList());

        /** 角色buffTool数据 **/
        for(Map.Entry<Integer, UserBuffToolDTO> entryBuffTool : userBuffToolDTOMap.entrySet()){
            UserBuffToolDTO userBuffToolDTO = entryBuffTool.getValue();
            BuffToolInfoPb buffToolInfoPb = new BuffToolInfoPb();

            /** buffTool配置数据 **/
            Map<Integer, CfgBuffToolDTO> cfgBuffToolDTOMap = CfgManagerSingleton.getInstance().getCfgBuffToolDTOMap();
            CfgBuffToolDTO cfgBuffToolDTO = cfgBuffToolDTOMap.get(userBuffToolDTO.getBuffToolId());
            buffToolInfoPb.setBuffToolId(cfgBuffToolDTO.getBuffToolId()).setDurations(cfgBuffToolDTO.getDurations());
            List<EffectAttributeInfoPb> effectAttributeInfoPbList = new ArrayList<>();
            JSONArray jsonArrayEffectAttributeInfo = JSONArray.parseArray(cfgBuffToolDTO.getEffectAttributeInfo());
            for(int i = 0;i<jsonArrayEffectAttributeInfo.size();i++){
                EffectAttributeInfoPb effectAttributeInfoPb = new EffectAttributeInfoPb();
                effectAttributeInfoPb.setAttributeType(jsonArrayEffectAttributeInfo.getJSONObject(i).getInteger("attributeType"));
                effectAttributeInfoPb.setMultiple(jsonArrayEffectAttributeInfo.getJSONObject(i).getFloat("multiple"));
                effectAttributeInfoPbList.add(effectAttributeInfoPb);
            }
            buffToolInfoPb.setEffectAttributeInfoPbList(effectAttributeInfoPbList);

            loginResPb.getBuffToolInfoPbList().add(buffToolInfoPb);
        }

        /** 宠物离线时间：单位秒 **/
        loginResPb.setOfflineTime( (userDTO.getLatestLoginTime().getTime() - userDTO.getLatestLogoutTime().getTime())/1000 );
        /** 宠物离线收益 **/
        long petOfflineIncome = settlementExecutor.petOfflineIncome(userDTO,userAttributeDTO,1);
        loginResPb.setPetOfflineIncome(petOfflineIncome);

        return loginResPb;
    }

    private void addUserVehicle(long userId,boolean isInUse, CfgVehicleDTO cfgVehicleDTO){
        UserVehicleDTO newUserVehicleDTO = new UserVehicleDTO();
        boolean isUnlocked = cfgVehicleDTO.getUnlockConditionCount() == 0 ? true : false;
        newUserVehicleDTO.setUserId(userId).setVehicleId(cfgVehicleDTO.getVehicleId()).setUnlocked(isUnlocked).setInUse(isInUse);
        userService.addUserVehicleToDB(newUserVehicleDTO);
        // 缓存存在，则缓存数据中也新增
        UserManagerSingleton.getInstance().addUserVehicleToCache(userId,newUserVehicleDTO.getVehicleId(),newUserVehicleDTO);
    }

    private void addUserEquipment(long userId,CfgEquipmentDTO cfgEquipmentDTO){
        UserEquipmentDTO newUserEquipmentDTO = new UserEquipmentDTO();
        boolean isUnlocked = false;
        if(cfgEquipmentDTO.getUnlockConditionCount() == 0){
            isUnlocked = true;
        }
        newUserEquipmentDTO.setUserId(userId).setEquipmentId(cfgEquipmentDTO.getEquipmentId()).setInUse(false).setUnlocked(isUnlocked);
        userService.addUserEquipmentToDB(newUserEquipmentDTO);
        // 缓存存在，则缓存数据中也新增
        UserManagerSingleton.getInstance().addUserEquipmentToCache(userId,newUserEquipmentDTO.getEquipmentId(),newUserEquipmentDTO);
    }

    private void addUserBuffTool(long userId,CfgBuffToolDTO cfgBuffToolDTO){
        UserBuffToolDTO newUserBuffToolDTO = new UserBuffToolDTO();
        newUserBuffToolDTO.setUserId(userId).setBuffToolId(cfgBuffToolDTO.getBuffToolId()).setInUse(false);
        userService.addUserBuffToolToDB(newUserBuffToolDTO);
        // 缓存存在，则缓存数据中也新增
        UserManagerSingleton.getInstance().addUserBuffToolToCache(userId,newUserBuffToolDTO.getBuffToolId(),newUserBuffToolDTO);
    }

    /**
     * 老角色登录检测是否有新的载具模版数据需处理
     * @param userId
     */
    private void checkCfgVehicleOnOldUserLogin(long userId){
        List<CfgVehicleDTO> newCfgVehicleDTOList = new ArrayList<>();
        Map<Integer,CfgVehicleDTO> cfgVehicleDTOMap = CfgManagerSingleton.getInstance().getCfgVehicleDTOMap();
        for(Map.Entry<Integer,CfgVehicleDTO> entry : cfgVehicleDTOMap.entrySet()){
            Map<Integer,UserVehicleDTO> userVehicleDTOMap = UserManagerSingleton.getInstance().getUserVehicleMapByIdFromCache(userId);
            if(userVehicleDTOMap.get(entry.getKey()) == null){
                newCfgVehicleDTOList.add(entry.getValue());
            }
        }
        // 新增的载具模版需要添加到角色载具数据中
        for(CfgVehicleDTO cfgVehicleDTO : newCfgVehicleDTOList){
            addUserVehicle(userId,false,cfgVehicleDTO);
        }
    }

    /**
     * 老用户登录检测是否有新的装备模版数据需处理
     * @param userId
     */
    private void checkCfgEquipmentOnOldUserLogin(long userId){
        List<CfgEquipmentDTO> newCfgEquipmentDTOList = new ArrayList<>();
        Map<Integer,CfgEquipmentDTO> cfgEquipmentDTOMap = CfgManagerSingleton.getInstance().getCfgEquipmentDTOMap();
        for(Map.Entry<Integer,CfgEquipmentDTO> entry : cfgEquipmentDTOMap.entrySet()){
            Map<Integer,UserEquipmentDTO> userEquipmentDTOMap = UserManagerSingleton.getInstance().getUserEquipmentMapByIdFromCache(userId);
            if(userEquipmentDTOMap.get(entry.getKey()) == null){
                newCfgEquipmentDTOList.add(entry.getValue());
            }
        }
        // 新增的装备模版需要添加到角色装备数据中
        for(CfgEquipmentDTO cfgEquipmentDTO : newCfgEquipmentDTOList){
            addUserEquipment(userId,cfgEquipmentDTO);
        }
    }

    /**
     * 老用户登录检测是否有新的BuffTool模版数据需处理
     * @param userId
     */
    private void checkCfgBuffToolOnOldUserLogin(long userId){
        List<CfgBuffToolDTO> newCfgBuffToolDTOList = new ArrayList<>();
        Map<Integer,CfgBuffToolDTO> cfgBuffToolDTOMap = CfgManagerSingleton.getInstance().getCfgBuffToolDTOMap();
        for(Map.Entry<Integer,CfgBuffToolDTO> entry : cfgBuffToolDTOMap.entrySet()){
            Map<Integer,UserBuffToolDTO> userBuffToolDTOMap = UserManagerSingleton.getInstance().getUserBuffToolMapByIdFromCache(userId);
            if(userBuffToolDTOMap.get(entry.getKey()) == null){
                newCfgBuffToolDTOList.add(entry.getValue());
            }
        }
        // 新增的buffTool模版需要添加到角色buffTool数据中
        for(CfgBuffToolDTO cfgBuffToolDTO : newCfgBuffToolDTOList){
            addUserBuffTool(userId,cfgBuffToolDTO);
        }
    }

    /**
     * 用户绑定逻辑服：此用户所有报文都会转发至绑定的逻辑服
     * @param userId
     */
    private boolean userBindServerId(long userId){
        try{
            // 添加需要绑定的用户（玩家）
            List<Long> userIdList = new ArrayList<>();
            userIdList.add(userId);

            // 绑定消息
            EndPointLogicServerMessage endPointLogicServerMessage = new EndPointLogicServerMessage()
                    // 需要绑定的玩家，示例中只取了当前请求匹配的玩家
                    .setUserList(userIdList)
                    // 需要绑定的逻辑服id
                    .setLogicServerId(CfgManagerSingleton.getInstance().getServerId())
                    // true 为绑定，false 为取消绑定
                    .setBinding(true);

            // 发送消息到网关
            ProcessorContext processorContext = BrokerClientHelper.getProcessorContext();
            processorContext.invokeOneway(endPointLogicServerMessage);

            log.info("LoginServiceImpl::userBindServerId:userId = {},serverId = {}用户绑定逻辑服成功",
                    userId,CfgManagerSingleton.getInstance().getServerId());
            return  true;
        }catch (Exception e){
            log.error("LoginServiceImpl::userBindServerId:userId = {},serverId = {},message = {},用户绑定逻辑服失败",
                    userId,CfgManagerSingleton.getInstance().getServerId(),e.getMessage());
            return false;
        }
    }

    @Override
    public void Logout(MyFlowContext myFlowContext){
        long userId = myFlowContext.getUserId();
        userService.saveDataFromCacheToDB(userId);
        log.info("LoginServiceImpl::Logout:userId = {},用户登出",userId);
    }
}
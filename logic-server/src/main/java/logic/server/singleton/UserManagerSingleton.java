package logic.server.singleton;

import com.alibaba.fastjson.JSONObject;
import logic.server.dto.CfgBuffToolDTO;
import logic.server.dto.CfgEquipmentDTO;
import logic.server.dto.CfgMagnateDTO;
import logic.server.dto.CfgVipDTO;
import logic.server.dto.UserAttributeDTO;
import logic.server.dto.UserBossDTO;
import logic.server.dto.UserBuffToolDTO;
import logic.server.dto.UserDTO;
import logic.server.dto.UserDrawDTO;
import logic.server.dto.UserEquipmentDTO;
import logic.server.dto.UserMagnateDTO;
import logic.server.dto.UserVehicleDTO;
import logic.server.dto.UserVehicleNewDTO;
import logic.server.dto.UserVipDTO;
import logic.server.enums.AttributeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.management.Attribute;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class UserManagerSingleton {
    private static UserManagerSingleton instance;
    private Map<Long, UserDTO> allUserDTOMap;
    private Map<Long, UserAttributeDTO> allUserAttributeDTOMap;
    private Map<Long, Map<Integer,UserVehicleDTO> > allUserVehicleDTOMap;
    private Map<Long, Map<Integer,UserVehicleNewDTO> > allUserVehicleNewDTOMap;
    private Map<Long,Map<Integer, UserEquipmentDTO> > allUserEquipmentDTOMap;
    private Map<Long,Map<Integer, UserBuffToolDTO> > allUserBuffToolDTOMap;
    private Map<Long,Map<Integer, UserMagnateDTO> > allUserMagnateDTOMap;
    private Map<Long,Map<Integer, UserBossDTO> > allUserBossDTOMap;
    private Map<Long, UserVipDTO> allUserVipDTOMap;
    private Map<Long, UserDrawDTO> allUserDrawDTOMap;

    public static synchronized UserManagerSingleton getInstance() {
        if (instance == null) {
            instance = new UserManagerSingleton();
        }
        return instance;
    }
    public UserManagerSingleton() {
        allUserDTOMap = new HashMap<>();
        allUserAttributeDTOMap = new HashMap<>();
        allUserVehicleDTOMap = new HashMap<>();
        allUserVehicleNewDTOMap = new HashMap<>();
        allUserEquipmentDTOMap = new HashMap<>();
        allUserBuffToolDTOMap = new HashMap<>();
        allUserMagnateDTOMap = new HashMap<>();
        allUserBossDTOMap = new HashMap<>();
        allUserVipDTOMap = new HashMap<>();
        allUserDrawDTOMap = new HashMap<>();
    }

    /** userDTO start **/
    public boolean addUserDataToCache(long userId, UserDTO userDTO, UserAttributeDTO userAttributeDTO,
                                      Map<Integer,UserVehicleDTO> userVehicleDTOMap, Map<Integer,UserVehicleNewDTO> userVehicleNewDTOMap,
                                      Map<Integer,UserEquipmentDTO> userEquipmentDTOMap, Map<Integer,UserBuffToolDTO> userBuffToolDTOMap,
                                      Map<Integer,UserMagnateDTO> userMagnateDTOMap, Map<Integer,UserBossDTO> userBossDTOMap, UserVipDTO userVipDTO){
        boolean isSuccess = true;
        try{
            addUserToCache(userId,userDTO);
            addUserAttributeToCache(userId,userAttributeDTO);
            addUserVehicleMapToCache(userId,userVehicleDTOMap);
            addUserVehicleNewMapToCache(userId,userVehicleNewDTOMap);
            addUserEquipmentMapToCache(userId,userEquipmentDTOMap);
            addUserBuffToolMapToCache(userId,userBuffToolDTOMap);
            addUserMagnateMapToCache(userId,userMagnateDTOMap);
            addUserBossMapToCache(userId,userBossDTOMap);
            addUserVipToCache(userId,userVipDTO);
            log.info("UserManagerSingleton::addUserDataToCache:userId = {},用户数据存储至内存成功",userId);
        }catch (Exception e){
            isSuccess = false;
            log.error("UserManagerSingleton::addUserDataToCache:userId = {},用户数据存储至内存失败",userId);
        }

        return isSuccess;
    }
    public void addUserToCache(long userId,UserDTO userDTO){
        allUserDTOMap.put(userId,userDTO);
    }
    public void removeUserInCache(long userId){
        allUserDTOMap.remove(userId);
    }
    public UserDTO getUserByIdFromCache(long userId){
        return allUserDTOMap.get(userId);
    }
    public Map<Long, UserDTO> getAllUserDTOMapFromCache(){return allUserDTOMap;}
    /** userDTO end **/

    /** UserAttributeDTO start **/
    public void addUserAttributeToCache(long userId,UserAttributeDTO userAttributeDTO){
        allUserAttributeDTOMap.put(userId,userAttributeDTO);
    }
    public void removeUserAttributeInCache(long userId){
        allUserAttributeDTOMap.remove(userId);
    }
    public UserAttributeDTO getUserAttributeFromCache(long userId){
        return allUserAttributeDTOMap.get(userId);
    }
    // 获取角色收益倍数属性：通过当前装备和buff加成计算获得
    public float getUserIncomeMultipleAttributeFromCache(long userId){
        float inComeMultiple = 1.0f;

        /** 装备-收益倍数加成 **/
        List<UserEquipmentDTO> userEquipmentDTOList = getUserEquipmentListByAttributeTypeFromCache(userId, AttributeEnum.incomeMultiple.getAttributeType());
        for(UserEquipmentDTO userEquipmentDTO : userEquipmentDTOList){
            if(!userEquipmentDTO.isInUse()) continue;
            CfgEquipmentDTO cfgEquipmentDTO = CfgManagerSingleton.getInstance().getCfgEquipmentByIdFromCache(userEquipmentDTO.getEquipmentId());
            inComeMultiple *= cfgEquipmentDTO.getEffectAttributeMultiple();
        }
        /** buffTool-收益倍数加成 **/
        List<UserBuffToolDTO> userBuffToolDTOList = getUserBuffToolListByAttributeTypeFromCache(userId, AttributeEnum.incomeMultiple.getAttributeType());
        for(UserBuffToolDTO userBuffToolDTO : userBuffToolDTOList){
            if(!userBuffToolDTO.isInUse()) continue;
            if(userBuffToolDTO.getEffectLeftTime() <= 0) continue;
            CfgBuffToolDTO cfgBuffToolDTO = CfgManagerSingleton.getInstance().getCfgBuffToolByIdFromCache(userBuffToolDTO.getBuffToolId());
            float buffToolMultiple = 1.0f;
            for(int i=0;i<cfgBuffToolDTO.getJsonArrayEffectAttributeInfo().size();i++){
                if(cfgBuffToolDTO.getJsonArrayEffectAttributeInfo().getJSONObject(i).getInteger("attributeType") == AttributeEnum.incomeMultiple.getAttributeType()){
                    buffToolMultiple = cfgBuffToolDTO.getJsonArrayEffectAttributeInfo().getJSONObject(i).getFloat("multiple");
                    break;
                }
            }
            inComeMultiple *= buffToolMultiple;
        }
        /** vip系统-收益倍数加成 **/
        // vip等级效果中是否有宠物搬运金额加成
        UserVipDTO userVipDTO = UserManagerSingleton.getInstance().getUserVipFromCache(userId);
        if(userVipDTO != null){
            CfgVipDTO cfgVipDTO = CfgManagerSingleton.getInstance().getCfgVipByVipLevelFromCache(userVipDTO.getVipLevel());
            float vipMultiple = 1.0f;
            if(cfgVipDTO != null){
                for(int i=0;i<cfgVipDTO.getJsonArrayEffectAttributeInfo().size();i++){
                    JSONObject jsonEffectAttributeInfo = cfgVipDTO.getJsonArrayEffectAttributeInfo().getJSONObject(i);
                    int attributeType = jsonEffectAttributeInfo.getIntValue("attributeType");
                    if(attributeType == AttributeEnum.incomeMultiple.getAttributeType()){
                        vipMultiple = jsonEffectAttributeInfo.getFloatValue("multiple");
                        break;
                    }
                }
            }
            inComeMultiple *= vipMultiple;
        }

        return inComeMultiple;
    }
    /** UserAttributeDTO end **/

    /** UserVehicleDTO start **/
    public void addUserVehicleMapToCache(long userId, Map<Integer,UserVehicleDTO> userVehicleDTOMap){
        allUserVehicleDTOMap.put(userId,userVehicleDTOMap);
    }
    public void addUserVehicleToCache(long userId,int vehicleId,UserVehicleDTO userVehicleDTO){
        Map<Integer,UserVehicleDTO> userVehicleDTOMap = getUserVehicleMapByIdFromCache(userId);
        if(userVehicleDTOMap != null){
            userVehicleDTOMap.put(vehicleId,userVehicleDTO);
        }
    }
    public void removeUserVehicleMapInCache(long userId){
        allUserVehicleDTOMap.remove(userId);
    }
    public Map<Integer,UserVehicleDTO> getUserVehicleMapByIdFromCache(long userId){
        return allUserVehicleDTOMap.get(userId);
    }
    public UserVehicleDTO getUserUsingVehicleByIdFromCache(long userId){
        Map<Integer,UserVehicleDTO> userVehicleDTOMap = getUserVehicleMapByIdFromCache(userId);
        for(Map.Entry<Integer,UserVehicleDTO> enter : userVehicleDTOMap.entrySet()){
            if(enter.getValue().isInUse()){
                return enter.getValue();
            }
        }
        return null;
    }
    public UserVehicleDTO getUserVehicleByIdFromCache(long userId,int vehicleId){
        Map<Integer,UserVehicleDTO> userVehicleDTOMap = getUserVehicleMapByIdFromCache(userId);
        return userVehicleDTOMap.get(vehicleId);
    }
    /** UserVehicleDTO end **/

    /** UserVehicleNewDTO start **/
    public void addUserVehicleNewMapToCache(long userId, Map<Integer,UserVehicleNewDTO> userVehicleNewDTOMap){
        allUserVehicleNewDTOMap.put(userId,userVehicleNewDTOMap);
    }
    public void addUserVehicleNewToCache(long userId, int vehicleId, UserVehicleNewDTO userVehicleNewDTO){
        Map<Integer,UserVehicleNewDTO> userVehicleNewDTOMap = getUserVehicleNewMapByIdFromCache(userId);
        if(userVehicleNewDTOMap != null){
            userVehicleNewDTOMap.put(vehicleId,userVehicleNewDTO);
        }
    }
    public void removeUserVehicleNewMapInCache(long userId){
        allUserVehicleNewDTOMap.remove(userId);
    }
    public Map<Integer,UserVehicleNewDTO> getUserVehicleNewMapByIdFromCache(long userId){
        return allUserVehicleNewDTOMap.get(userId);
    }
    public UserVehicleNewDTO getUserVehicleNewByIdFromCache(long userId,int vehicleId){
        Map<Integer,UserVehicleNewDTO> userVehicleNewDTOMap = getUserVehicleNewMapByIdFromCache(userId);
        return userVehicleNewDTOMap.get(vehicleId);
    }
    /** UserVehicleNewDTO end **/

    /** UserEquipmentDTO start **/
    public void addUserEquipmentMapToCache(long userId, Map<Integer,UserEquipmentDTO> userEquipmentDTOMap){
        allUserEquipmentDTOMap.put(userId,userEquipmentDTOMap);
    }
    public void addUserEquipmentToCache(long userId,int equipmentId,UserEquipmentDTO userEquipmentDTO){
        Map<Integer,UserEquipmentDTO> userEquipmentDTOMap = getUserEquipmentMapByIdFromCache(userId);
        if(userEquipmentDTOMap != null){
            userEquipmentDTOMap.put(equipmentId,userEquipmentDTO);
        }
    }
    public void removeUserEquipmentMapInCache(long userId){
        allUserEquipmentDTOMap.remove(userId);
    }
    public Map<Integer,UserEquipmentDTO> getUserEquipmentMapByIdFromCache(long userId){
        return allUserEquipmentDTOMap.get(userId);
    }
    public UserEquipmentDTO getUserEquipmentByIdFromCache(long userId,int equipmentId){
        Map<Integer,UserEquipmentDTO> userEquipmentDTOMap = getUserEquipmentMapByIdFromCache(userId);
        return userEquipmentDTOMap.get(equipmentId);
    }
    public List<UserEquipmentDTO> getUserEquipmentListByAttributeTypeFromCache(long userId, int attributeType){
        Map<Integer,UserEquipmentDTO> userEquipmentDTOMap = getUserEquipmentMapByIdFromCache(userId);
        List<UserEquipmentDTO> userEquipmentDTOList = new ArrayList<>();
        for(Map.Entry<Integer,UserEquipmentDTO> entry : userEquipmentDTOMap.entrySet()){
            CfgEquipmentDTO cfgEquipmentDTO = CfgManagerSingleton.getInstance().getCfgEquipmentByIdFromCache(entry.getKey());
            if(cfgEquipmentDTO != null && cfgEquipmentDTO.getEffectAttributeType() == attributeType){
                userEquipmentDTOList.add(entry.getValue());
            }
        }
        return userEquipmentDTOList;
    }
    /** UserEquipmentDTO end **/

    /** UserBuffToolDTO start **/
    public void addUserBuffToolMapToCache(long userId, Map<Integer,UserBuffToolDTO> userBuffToolDTOMap){
        allUserBuffToolDTOMap.put(userId,userBuffToolDTOMap);
    }
    public void addUserBuffToolToCache(long userId,int buffToolId,UserBuffToolDTO userBuffToolDTO){
        Map<Integer,UserBuffToolDTO> userBuffToolDTOMap = getUserBuffToolMapByIdFromCache(userId);
        if(userBuffToolDTOMap != null){
            userBuffToolDTOMap.put(buffToolId,userBuffToolDTO);
        }
    }
    public void removeUserBuffToolMapInCache(long userId){
        allUserBuffToolDTOMap.remove(userId);
    }
    public Map<Integer,UserBuffToolDTO> getUserBuffToolMapByIdFromCache(long userId){
        return allUserBuffToolDTOMap.get(userId);
    }
    public UserBuffToolDTO getUserBuffToolByIdFromCache(long userId,int buffToolId){
        Map<Integer,UserBuffToolDTO> userBuffToolDTOMap = getUserBuffToolMapByIdFromCache(userId);
        return userBuffToolDTOMap.get(buffToolId);
    }
    public List<UserBuffToolDTO> getUserBuffToolListByAttributeTypeFromCache(long userId, int attributeType){
        Map<Integer,UserBuffToolDTO> userBuffToolDTOMap = getUserBuffToolMapByIdFromCache(userId);
        List<UserBuffToolDTO> userBuffToolDTOList = new ArrayList<>();
        for(Map.Entry<Integer,UserBuffToolDTO> entry : userBuffToolDTOMap.entrySet()){
            CfgBuffToolDTO cfgBuffToolDTO = CfgManagerSingleton.getInstance().getCfgBuffToolByIdFromCache(entry.getKey());
            if(cfgBuffToolDTO != null){
                for(int i=0;i<cfgBuffToolDTO.getJsonArrayEffectAttributeInfo().size();i++){
                    if(cfgBuffToolDTO.getJsonArrayEffectAttributeInfo().getJSONObject(i).getInteger("attributeType") == attributeType){
                        userBuffToolDTOList.add(entry.getValue());
                        break;
                    }
                }
            }
        }
        return userBuffToolDTOList;
    }
    /** UserBuffToolDTO end **/

    /** UserMagnateDTO start **/
    public void addUserMagnateMapToCache(long userId, Map<Integer,UserMagnateDTO> userMagnateDTOMap){
        allUserMagnateDTOMap.put(userId,userMagnateDTOMap);
    }
    public void addUserMagnateToCache(long userId, int magnateId, UserMagnateDTO userMagnateDTO){
        Map<Integer,UserMagnateDTO> userMagnateDTOMap = getUserMagnateMapByIdFromCache(userId);
        if(userMagnateDTOMap != null){
            userMagnateDTOMap.put(magnateId,userMagnateDTO);
        }
    }
    public void removeUserMagnateMapInCache(long userId){
        allUserMagnateDTOMap.remove(userId);
    }
    public Map<Integer,UserMagnateDTO> getUserMagnateMapByIdFromCache(long userId){
        return allUserMagnateDTOMap.get(userId);
    }
    public UserMagnateDTO getUserMagnateByIdFromCache(long userId,int magnateId){
        Map<Integer,UserMagnateDTO> userMagnateDTOMap = getUserMagnateMapByIdFromCache(userId);
        return userMagnateDTOMap.get(magnateId);
    }
    // 已击败的显示最考后的magnate
    public UserMagnateDTO getUserLatestBeatMagnateFromCache(long userId){
        UserMagnateDTO latestUserMagnateDTO = null;

        Map<Integer,UserMagnateDTO> userMagnateDTOMap = getUserMagnateMapByIdFromCache(userId);
        int showIndex = 0;
        for(Map.Entry<Integer,UserMagnateDTO> entry : userMagnateDTOMap.entrySet()){
            if(entry.getValue().isBeat()){
                CfgMagnateDTO cfgMagnateDTO = CfgManagerSingleton.getInstance().getCfgMagnateByIdFromCache(entry.getKey());
                if(cfgMagnateDTO.getShowIndex() >= showIndex){
                    latestUserMagnateDTO = entry.getValue();
                    showIndex = cfgMagnateDTO.getShowIndex();
                }
            }
        }

        return latestUserMagnateDTO;
    }
    /** UserMagnateDTO end **/

    /** UserBossDTO start **/
    public void addUserBossMapToCache(long userId, Map<Integer,UserBossDTO> userBossDTOMap){
        allUserBossDTOMap.put(userId,userBossDTOMap);
    }
    public void addUserBossToCache(long userId, int bossId, UserBossDTO userBossDTO){
        Map<Integer,UserBossDTO> userBossDTOMap = getUserBossMapByIdFromCache(userId);
        if(userBossDTOMap != null){
            userBossDTOMap.put(bossId,userBossDTO);
        }
    }
    public void removeUserBossMapInCache(long userId){
        allUserBossDTOMap.remove(userId);
    }
    public Map<Integer,UserBossDTO> getUserBossMapByIdFromCache(long userId){
        return allUserBossDTOMap.get(userId);
    }
    public UserBossDTO getUserBossByIdFromCache(long userId,int bossId){
        Map<Integer,UserBossDTO> userBossDTOMap = getUserBossMapByIdFromCache(userId);
        return userBossDTOMap.get(bossId);
    }
    /** UserBossDTO end **/

    /** UserVipDTO start **/
    public void addUserVipToCache(long userId,UserVipDTO userVipDTO){
        allUserVipDTOMap.put(userId,userVipDTO);
    }
    public UserVipDTO getUserVipFromCache(long userId){
        return allUserVipDTOMap.get(userId);
    }
    public void removeUserVipInCache(long userId){
        allUserVipDTOMap.remove(userId);
    }
    /** UserVipDTO end **/

    /** UserDrawDTO start **/
    public void addUserDrawToCache(long userId,UserDrawDTO userDrawDTO){
        allUserDrawDTOMap.put(userId,userDrawDTO);
    }
    public UserDrawDTO getUserDrawFromCache(long userId){
        return allUserDrawDTOMap.get(userId);
    }
    public void removeUserDrawInCache(long userId){
        allUserDrawDTOMap.remove(userId);
    }
    /** UserDrawDTO end **/
}

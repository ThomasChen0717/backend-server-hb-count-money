package logic.server.singleton;

import logic.server.dto.CfgBuffToolDTO;
import logic.server.dto.CfgEquipmentDTO;
import logic.server.dto.UserAttributeDTO;
import logic.server.dto.UserBuffToolDTO;
import logic.server.dto.UserDTO;
import logic.server.dto.UserEquipmentDTO;
import logic.server.dto.UserMagnateDTO;
import logic.server.dto.UserVehicleDTO;
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
    private Map<Long,Map<Integer, UserEquipmentDTO> > allUserEquipmentDTOMap;
    private Map<Long,Map<Integer, UserBuffToolDTO> > allUserBuffToolDTOMap;
    private Map<Long,Map<Integer, UserMagnateDTO> > allUserMagnateDTOMap;

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
        allUserEquipmentDTOMap = new HashMap<>();
        allUserBuffToolDTOMap = new HashMap<>();
        allUserMagnateDTOMap = new HashMap<>();
    }

    /** userDTO start **/
    public boolean addUserDataToCache(long userId,UserDTO userDTO,UserAttributeDTO userAttributeDTO,
                                      Map<Integer,UserVehicleDTO> userVehicleDTOMap,Map<Integer,UserEquipmentDTO> userEquipmentDTOMap,
                                      Map<Integer,UserBuffToolDTO> userBuffToolDTOMap,Map<Integer,UserMagnateDTO> userMagnateDTOMap ){
        boolean isSuccess = true;
        try{
            addUserToCache(userId,userDTO);
            addUserAttributeToCache(userId,userAttributeDTO);
            addUserVehicleMapToCache(userId,userVehicleDTOMap);
            addUserEquipmentMapToCache(userId,userEquipmentDTOMap);
            addUserBuffToolMapToCache(userId,userBuffToolDTOMap);
            addUserMagnateMapToCache(userId,userMagnateDTOMap);
            log.info("UserManagerSingleton::addUserDataToCache:userId = {},用户数据存储至内存成功",userId);
        }catch (Exception e){
            isSuccess = false;
            log.error("UserManagerSingleton::addUserDataToCache:userId = {},用户数据存储至内存成功",userId);
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
    /** UserMagnateDTO end **/
}

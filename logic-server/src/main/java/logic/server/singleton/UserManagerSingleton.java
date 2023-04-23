package logic.server.singleton;

import logic.server.dto.CfgEquipmentDTO;
import logic.server.dto.UserAttributeDTO;
import logic.server.dto.UserDTO;
import logic.server.dto.UserEquipmentDTO;
import logic.server.dto.UserVehicleDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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
    }

    public boolean addUserDataToCache(long userId,UserDTO userDTO,UserAttributeDTO userAttributeDTO,
                                      Map<Integer,UserVehicleDTO> userVehicleDTOMap,Map<Integer,UserEquipmentDTO> userEquipmentDTOMap){
        boolean isSuccess = true;

        try{
            addUserToCache(userId,userDTO);
            addUserAttributeToCache(userId,userAttributeDTO);
            addUserVehicleMapToCache(userId,userVehicleDTOMap);
            addUserEquipmentMapToCache(userId,userEquipmentDTOMap);
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

    public void addUserAttributeToCache(long userId,UserAttributeDTO userAttributeDTO){
        allUserAttributeDTOMap.put(userId,userAttributeDTO);
    }
    public void removeUserAttributeInCache(long userId){
        allUserAttributeDTOMap.remove(userId);
    }
    public UserAttributeDTO getUserAttributeFromCache(long userId){
        return allUserAttributeDTOMap.get(userId);
    }

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
    public List<UserEquipmentDTO> getEquipmentListByAttributeTypeFromCache(long userId, int attributeType){
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
}

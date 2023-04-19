package logic.server.singleton;

import logic.server.dto.UserAttributeDTO;
import logic.server.dto.UserDTO;
import logic.server.dto.UserEquipmentDTO;
import logic.server.dto.UserVehicleDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
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
            addUserVehicleToCache(userId,userVehicleDTOMap);
            addUserEquipmentToCache(userId,userEquipmentDTOMap);
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
    public void removeUserToCache(long userId){
        allUserDTOMap.remove(userId);
    }
    public UserDTO getUserByIdFromCache(long userId){
        return allUserDTOMap.get(userId);
    }

    public void addUserAttributeToCache(long userId,UserAttributeDTO userAttributeDTO){
        allUserAttributeDTOMap.put(userId,userAttributeDTO);
    }
    public void removeUserAttributeToCache(long userId){
        allUserAttributeDTOMap.remove(userId);
    }
    public UserAttributeDTO getUserAttributeFromCache(long userId){
        return allUserAttributeDTOMap.get(userId);
    }

    public void addUserVehicleToCache(long userId,Map<Integer,UserVehicleDTO> userVehicleDTOMap){
        allUserVehicleDTOMap.put(userId,userVehicleDTOMap);
    }
    public void removeUserVehicleToCache(long userId){
        allUserVehicleDTOMap.remove(userId);
    }
    public Map<Integer,UserVehicleDTO> getUserVehicleByIdFromCache(long userId){
        return allUserVehicleDTOMap.get(userId);
    }

    public void addUserEquipmentToCache(long userId,Map<Integer,UserEquipmentDTO> userEquipmentDTOMap){
        allUserEquipmentDTOMap.put(userId,userEquipmentDTOMap);
    }
    public void removeUserEquipmentToCache(long userId){
        allUserEquipmentDTOMap.remove(userId);
    }
    public Map<Integer,UserEquipmentDTO> getUserEquipmentByIdFromCache(long userId){
        return allUserEquipmentDTOMap.get(userId);
    }
}

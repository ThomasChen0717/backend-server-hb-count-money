package logic.server.singleton;

import logic.server.dto.UserAttributeDTO;
import logic.server.dto.UserDTO;
import logic.server.dto.UserVehicleDTO;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class UserManagerSingleton {
    private static UserManagerSingleton instance;
    private Map<Long, UserDTO> userDTOMap;
    private Map<Long, UserAttributeDTO> userAttributeDTOMap;
    private Map<Long, UserVehicleDTO> userVehicleDTOMap;

    public static synchronized UserManagerSingleton getInstance() {
        if (instance == null) {
            instance = new UserManagerSingleton();
        }
        return instance;
    }
    private UserManagerSingleton() {
        userDTOMap = new HashMap<>();
        userAttributeDTOMap = new HashMap<>();
        userVehicleDTOMap = new HashMap<>();
    }

    @Transactional
    public boolean addUserDataToCache(long userId,UserDTO userDTO,UserAttributeDTO userAttributeDTO,UserVehicleDTO userVehicleDTO){
        boolean isSuccess = true;

        try{
            addUserToCache(userId,userDTO);
            addUserAttributeToCache(userId,userAttributeDTO);
            addUserVehicleToCache(userId,userVehicleDTO);
            log.info("UserManagerSingleton::addUserDataToCache:userId = {},用户数据存储至内存成功",userId);
        }catch (Exception e){
            isSuccess = false;
            log.error("UserManagerSingleton::addUserDataToCache:userId = {},用户数据存储至内存成功",userId);
        }

        return isSuccess;
    }

    public void addUserToCache(long userId,UserDTO userDTO){
        userDTOMap.put(userId,userDTO);
    }
    public void removeUserToCache(long userId){
        userDTOMap.remove(userId);
    }
    public UserDTO getUserByIdFromCache(long userId){
        return userDTOMap.get(userId);
    }

    public void addUserAttributeToCache(long userId,UserAttributeDTO userAttributeDTO){
        userAttributeDTOMap.put(userId,userAttributeDTO);
    }
    public void removeUserAttributeToCache(long userId){
        userAttributeDTOMap.remove(userId);
    }
    public UserAttributeDTO getUserAttributeFromCache(long userId){
        return userAttributeDTOMap.get(userId);
    }

    public void addUserVehicleToCache(long userId,UserVehicleDTO userVehicleDTO){
        userVehicleDTOMap.put(userId,userVehicleDTO);
    }
    public void removeUserVehicleToCache(long userId){
        userVehicleDTOMap.remove(userId);
    }
    public UserVehicleDTO getUserVehicleByIdFromCache(long userId){
        return userVehicleDTOMap.get(userId);
    }
}

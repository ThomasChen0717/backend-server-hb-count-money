package logic.server.service.impl;

import common.pb.cmd.UserCmdModule;
import logic.server.dto.UserAttributeDTO;
import logic.server.dto.UserDTO;
import logic.server.dto.UserEquipmentDTO;
import logic.server.dto.UserVehicleDTO;
import logic.server.repository.UserAttributeRepository;
import logic.server.repository.UserEquipmentRepository;
import logic.server.repository.UserRepository;
import logic.server.repository.UserVehicleRepository;
import logic.server.service.IUserService;
import logic.server.service.impl.action.AttributeLevelUpExecutor;
import logic.server.service.impl.action.BaseExecutor;
import logic.server.service.impl.action.ChangeVehicleExecutor;
import logic.server.service.impl.action.SettlementExecutor;
import logic.server.service.impl.action.UseEquipmentExecutor;
import logic.server.singleton.UserManagerSingleton;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
public class UserServiceImpl implements IUserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserAttributeRepository userAttributeRepository;
    @Autowired
    private UserVehicleRepository userVehicleRepository;
    @Autowired
    private UserEquipmentRepository userEquipmentRepository;

    /** 注入执行器-start **/
    @Autowired
    private SettlementExecutor settlementExecutor;
    @Autowired
    private AttributeLevelUpExecutor attributeLevelUpExecutor;
    @Autowired
    private ChangeVehicleExecutor changeVehicleExecutor;
    @Autowired
    private UseEquipmentExecutor useEquipmentExecutor;
    /** 注入执行器-end **/

    @Override
    public int addUserToDB(UserDTO userDTO){
        return userRepository.add(userDTO);
    }

    @Override
    public UserDTO getUserByIdFromDB(long id){
        return userRepository.get(id);
    }

    @Override
    public UserDTO getUserByUnionIdFromDB(String unionId){
        return userRepository.getByUnionId(unionId);
    }

    @Override
    public UserDTO getUserByTokenFromDB(String unionId){
        return userRepository.getByToken(unionId);
    }

    @Override
    public int updateUserToDB(UserDTO userDTO){
        return userRepository.update(userDTO);
    }

    @Override
    public int addUserAttributeToDB(UserAttributeDTO userAttributeDTO){
        return userAttributeRepository.add(userAttributeDTO);
    }
    @Override
    public UserAttributeDTO getUserAttributeByIdFromDB(long userId){
        return userAttributeRepository.get(userId);
    }

    @Override
    public int addUserVehicleToDB(UserVehicleDTO userVehicleDTO){
        return userVehicleRepository.add(userVehicleDTO);
    }
    @Override
    public Map<Integer,UserVehicleDTO> getUserVehicleMapByIdFromDB(long userId){
        return userVehicleRepository.getMap(userId);
    }

    @Override
    public int addUserEquipmentToDB(UserEquipmentDTO userEquipmentDTO){
        return userEquipmentRepository.add(userEquipmentDTO);
    }
    @Override
    public Map<Integer,UserEquipmentDTO> getUserEquipmentMapByIdFromDB(long userId){
        return userEquipmentRepository.getMap(userId);
    }

    @Override
    public void saveDataFromCacheToDB(long userId){
        try{
            /** save t_user **/
            UserDTO userDTO = UserManagerSingleton.getInstance().getUserByIdFromCache(userId);
            if(userDTO != null){
                userRepository.update(userDTO);
                UserManagerSingleton.getInstance().removeUserInCache(userId);
            }
            /** save t_user_attribute **/
            UserAttributeDTO userAttributeDTO = UserManagerSingleton.getInstance().getUserAttributeFromCache(userId);
            if(userAttributeDTO != null){
                userAttributeRepository.update(userAttributeDTO);
                UserManagerSingleton.getInstance().removeUserAttributeInCache(userId);
            }
            /** save t_user_vehicle **/
            Map<Integer,UserVehicleDTO> userVehicleDTOMap = UserManagerSingleton.getInstance().getUserVehicleMapByIdFromCache(userId);
            if(userVehicleDTOMap != null){
                for(Map.Entry<Integer,UserVehicleDTO> entryVehicle : userVehicleDTOMap.entrySet()){
                    userVehicleRepository.update(entryVehicle.getValue());
                }
                UserManagerSingleton.getInstance().removeUserVehicleMapInCache(userId);
            }
            /** save t_user_equipment **/
            Map<Integer,UserEquipmentDTO> userEquipmentDTOMap = UserManagerSingleton.getInstance().getUserEquipmentMapByIdFromCache(userId);
            if(userEquipmentDTOMap != null){
                for(Map.Entry<Integer,UserEquipmentDTO> entryEquipment : userEquipmentDTOMap.entrySet()){
                    userEquipmentRepository.update(entryEquipment.getValue());
                }
                UserManagerSingleton.getInstance().removeUserEquipmentMapInCache(userId);
            }
            log.info("UserServiceImpl::saveDataFromCacheToDB:userId = {},用户数据缓存至数据库保存成功",userId);
        }catch (Exception e){
            log.error("UserServiceImpl::saveDataFromCacheToDB:userId = {},message = {},用户数据缓存至数据库保存失败",userId,e.getMessage());
        }
    }

    @Override
    public BaseExecutor getExecutor(String executorName){
        if(executorName.compareTo(UserCmdModule.settlementExecutorName) == 0){
            return settlementExecutor;
        }else if(executorName.compareTo(UserCmdModule.attributeLevelUpExecutorName) == 0){
            return attributeLevelUpExecutor;
        }else if(executorName.compareTo(UserCmdModule.changeVehicleExecutorName) == 0){
            return changeVehicleExecutor;
        }else if(executorName.compareTo(UserCmdModule.useEquipmentExecutorName) == 0){
            return useEquipmentExecutor;
        }
        return null;
    }
}

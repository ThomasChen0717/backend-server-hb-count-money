package logic.server.service.impl;

import common.pb.cmd.UserCmdModule;
import logic.server.dto.UserAttributeDTO;
import logic.server.dto.UserBossDTO;
import logic.server.dto.UserBuffToolDTO;
import logic.server.dto.UserDTO;
import logic.server.dto.UserEquipmentDTO;
import logic.server.dto.UserMagnateDTO;
import logic.server.dto.UserVehicleDTO;
import logic.server.repository.UserAttributeRepository;
import logic.server.repository.UserBossRepository;
import logic.server.repository.UserBuffToolRepository;
import logic.server.repository.UserEquipmentRepository;
import logic.server.repository.UserMagnateRepository;
import logic.server.repository.UserRepository;
import logic.server.repository.UserVehicleRepository;
import logic.server.service.IUserService;
import logic.server.service.impl.action.AttributeLevelUpExecutor;
import logic.server.service.impl.action.BaseExecutor;
import logic.server.service.impl.action.ChallengeBossSuccessExecutor;
import logic.server.service.impl.action.ChallengeMagnateSuccessExecutor;
import logic.server.service.impl.action.ChangeVehicleExecutor;
import logic.server.service.impl.action.GetRedPacketExecutor;
import logic.server.service.impl.action.SettlementExecutor;
import logic.server.service.impl.action.UnlockVehicleOrEquipmentExecutor;
import logic.server.service.impl.action.UseEquipmentExecutor;
import logic.server.service.impl.action.StartOrEndBuffToolExecutor;
import logic.server.singleton.UserManagerSingleton;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Iterator;
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
    @Autowired
    private UserBuffToolRepository userBuffToolRepository;
    @Autowired
    private UserMagnateRepository userMagnateRepository;
    @Autowired
    private UserBossRepository userBossRepository;

    /** 注入执行器-start **/
    @Autowired
    private SettlementExecutor settlementExecutor;
    @Autowired
    private AttributeLevelUpExecutor attributeLevelUpExecutor;
    @Autowired
    private ChangeVehicleExecutor changeVehicleExecutor;
    @Autowired
    private UseEquipmentExecutor useEquipmentExecutor;
    @Autowired
    private StartOrEndBuffToolExecutor startOrEndBuffToolExecutor;
    @Autowired
    private GetRedPacketExecutor getRedPacketExecutor;
    @Autowired
    private ChallengeMagnateSuccessExecutor challengeMagnateSuccessExecutor;
    @Autowired
    private ChallengeBossSuccessExecutor challengeBossSuccessExecutor;
    @Autowired
    private UnlockVehicleOrEquipmentExecutor unlockVehicleOrEquipmentExecutor;
    /** 注入执行器-end **/

    /** t_user start **/
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
    /** t_user end **/

    /** t_user_attribute start **/
    @Override
    public int addUserAttributeToDB(UserAttributeDTO userAttributeDTO){
        return userAttributeRepository.add(userAttributeDTO);
    }
    @Override
    public UserAttributeDTO getUserAttributeByIdFromDB(long userId){
        return userAttributeRepository.get(userId);
    }
    /** t_user_attribute end **/

    /** t_user_vehicle start **/
    @Override
    public int addUserVehicleToDB(UserVehicleDTO userVehicleDTO){
        return userVehicleRepository.add(userVehicleDTO);
    }
    @Override
    public Map<Integer,UserVehicleDTO> getUserVehicleMapByIdFromDB(long userId){
        return userVehicleRepository.getMap(userId);
    }
    /** t_user_vehicle end **/

    /** t_user_equipment start **/
    @Override
    public int addUserEquipmentToDB(UserEquipmentDTO userEquipmentDTO){
        return userEquipmentRepository.add(userEquipmentDTO);
    }
    @Override
    public Map<Integer,UserEquipmentDTO> getUserEquipmentMapByIdFromDB(long userId){
        return userEquipmentRepository.getMap(userId);
    }
    /** t_user_equipment end **/

    /** t_user_buff_tool start **/
    @Override
    public int addUserBuffToolToDB(UserBuffToolDTO userBuffToolDTO){
        return userBuffToolRepository.add(userBuffToolDTO);
    }
    @Override
    public Map<Integer,UserBuffToolDTO> getUserBuffToolMapByIdFromDB(long userId){
        return userBuffToolRepository.getMap(userId);
    }
    /** t_user_buff_tool end **/

    /** t_user_magnate start **/
    @Override
    public int addUserMagnateToDB(UserMagnateDTO userMagnateDTO){
        return userMagnateRepository.add(userMagnateDTO);
    }
    @Override
    public Map<Integer,UserMagnateDTO> getUserMagnateMapByIdFromDB(long userId){
        return userMagnateRepository.getMap(userId);
    }
    /** t_user_magnate end **/

    /** t_user_boss start **/
    @Override
    public int addUserBossToDB(UserBossDTO userBossDTO){
        return userBossRepository.add(userBossDTO);
    }
    @Override
    public Map<Integer,UserBossDTO> getUserBossMapByIdFromDB(long userId){
        return userBossRepository.getMap(userId);
    }

    @Override
    public void saveDataFromCacheToDB(long userId){
        try{
            Date currTime = new Date();
            /** save t_user **/
            UserDTO userDTO = UserManagerSingleton.getInstance().getUserByIdFromCache(userId);
            if(userDTO != null){
                userDTO.setLatestLogoutTime(currTime);
                userDTO.setOnlineServerId(0);
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
            /** save t_user_buff_tool **/
            Map<Integer,UserBuffToolDTO> userBuffToolDTOMap = UserManagerSingleton.getInstance().getUserBuffToolMapByIdFromCache(userId);
            if(userBuffToolDTOMap != null){
                for(Map.Entry<Integer,UserBuffToolDTO> entryBuffTool : userBuffToolDTOMap.entrySet()){
                    userBuffToolRepository.update(entryBuffTool.getValue());
                }
                UserManagerSingleton.getInstance().removeUserBuffToolMapInCache(userId);
            }
            /** save t_user_magnate **/
            Map<Integer,UserMagnateDTO> userMagnateDTOMap = UserManagerSingleton.getInstance().getUserMagnateMapByIdFromCache(userId);
            if(userMagnateDTOMap != null){
                for(Map.Entry<Integer,UserMagnateDTO> entryMagnate : userMagnateDTOMap.entrySet()){
                    userMagnateRepository.update(entryMagnate.getValue());
                }
                UserManagerSingleton.getInstance().removeUserMagnateMapInCache(userId);
            }
            /** save t_user_boss **/
            Map<Integer,UserBossDTO> userBossDTOMap = UserManagerSingleton.getInstance().getUserBossMapByIdFromCache(userId);
            if(userBossDTOMap != null){
                for(Map.Entry<Integer,UserBossDTO> entryBoss : userBossDTOMap.entrySet()){
                    userBossRepository.update(entryBoss.getValue());
                }
                UserManagerSingleton.getInstance().removeUserBossMapInCache(userId);
            }
            log.info("UserServiceImpl::saveDataFromCacheToDB:userId = {},用户数据缓存至数据库保存成功",userId);
        }catch (Exception e){
            log.error("UserServiceImpl::saveDataFromCacheToDB:userId = {},message = {},用户数据缓存至数据库保存失败",userId,e.getMessage());
        }
    }

    @Override
    public void saveDataFromCacheToDB(){
        Map<Long, UserDTO> userDTOMap = UserManagerSingleton.getInstance().getAllUserDTOMapFromCache();
        for(Map.Entry<Long,UserDTO> entry : userDTOMap.entrySet()){
            saveDataFromCacheToDB(entry.getKey());
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
        }else if(executorName.compareTo(UserCmdModule.startOrEndBuffToolToolExecutorName) == 0){
            return startOrEndBuffToolExecutor;
        }else if(executorName.compareTo(UserCmdModule.getRedPacketExecutorName) == 0){
            return getRedPacketExecutor;
        }else if(executorName.compareTo(UserCmdModule.challengeMagnateSuccessExecutorName) == 0){
            return challengeMagnateSuccessExecutor;
        }else if(executorName.compareTo(UserCmdModule.challengeBossSuccessExecutorName) == 0){
            return challengeBossSuccessExecutor;
        }else if(executorName.compareTo(UserCmdModule.unlockVehicleOrEquipmentExecutorName) == 0){
            return unlockVehicleOrEquipmentExecutor;
        }
        return null;
    }
}

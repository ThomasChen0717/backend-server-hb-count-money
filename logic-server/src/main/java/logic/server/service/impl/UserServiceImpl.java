package logic.server.service.impl;

import com.baida.countmoney.client.command.ClientCommand;
import common.pb.cmd.UserCmdModule;
import logic.server.dto.UserAttributeDTO;
import logic.server.dto.UserBossDTO;
import logic.server.dto.UserBuffToolDTO;
import logic.server.dto.UserDTO;
import logic.server.dto.UserEquipmentDTO;
import logic.server.dto.UserMagnateDTO;
import logic.server.dto.UserVehicleDTO;
import logic.server.dto.UserVehicleNewDTO;
import logic.server.dto.UserVipDTO;
import logic.server.repository.UserAttributeRepository;
import logic.server.repository.UserBossRepository;
import logic.server.repository.UserBuffToolRepository;
import logic.server.repository.UserEquipmentRepository;
import logic.server.repository.UserMagnateRepository;
import logic.server.repository.UserRepository;
import logic.server.repository.UserVehicleNewRepository;
import logic.server.repository.UserVehicleRepository;
import logic.server.repository.UserVipRepository;
import logic.server.service.IUserService;
import logic.server.service.impl.action.AddClickCountExecutor;
import logic.server.service.impl.action.AttributeLevelUpExecutor;
import logic.server.service.impl.action.BaseExecutor;
import logic.server.service.impl.action.LotteryTicketBonusGetExecutor;
import logic.server.service.impl.action.LotteryTicketBuyExecutor;
import logic.server.service.impl.action.ChallengeBossSuccessExecutor;
import logic.server.service.impl.action.ChallengeMagnateSuccessExecutor;
import logic.server.service.impl.action.ChangeVehicleExecutor;
import logic.server.service.impl.action.GetRedPacketExecutor;
import logic.server.service.impl.action.GmCommandExecutor;
import logic.server.service.impl.action.LogicHeartbeatExecutor;
import logic.server.service.impl.action.SelectStoneExecutor;
import logic.server.service.impl.action.SellStoneExecutor;
import logic.server.service.impl.action.SettlementExecutor;
import logic.server.service.impl.action.UnlockVehicleOrEquipmentExecutor;
import logic.server.service.impl.action.UseEquipmentExecutor;
import logic.server.service.impl.action.StartOrEndBuffToolExecutor;
import logic.server.service.impl.action.VehicleNewLevelUpExecutor;
import logic.server.service.impl.action.WatchedAdExecutor;
import logic.server.singleton.CfgManagerSingleton;
import logic.server.singleton.UserManagerSingleton;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class UserServiceImpl implements IUserService,ApplicationEventPublisherAware {
    private ApplicationEventPublisher publisher;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserAttributeRepository userAttributeRepository;
    @Autowired
    private UserVehicleRepository userVehicleRepository;
    @Autowired
    private UserVehicleNewRepository userVehicleNewRepository;
    @Autowired
    private UserEquipmentRepository userEquipmentRepository;
    @Autowired
    private UserBuffToolRepository userBuffToolRepository;
    @Autowired
    private UserMagnateRepository userMagnateRepository;
    @Autowired
    private UserBossRepository userBossRepository;
    @Autowired
    private UserVipRepository userVipRepository;
    @Autowired
    private RedissonClient redissonClient;

    /**
     * 注入执行器-start
     **/
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
    @Autowired
    private LogicHeartbeatExecutor logicHeartbeatExecutor;
    @Autowired
    private GmCommandExecutor gmCommandExecutor;
    @Autowired
    private WatchedAdExecutor watchedAdExecutor;
    @Autowired
    private SelectStoneExecutor selectStoneExecutor;
    @Autowired
    private SellStoneExecutor sellStoneExecutor;
    @Autowired
    private AddClickCountExecutor addClickCountExecutor;
    @Autowired
    private VehicleNewLevelUpExecutor vehicleNewLevelUpExecutor;
    @Autowired
    private LotteryTicketBuyExecutor lotteryTicketBuyExecutor;
    @Autowired
    private LotteryTicketBonusGetExecutor lotteryTicketBonusGetExecutor;

    /** 注入执行器-end **/

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher publisher) {
        this.publisher = publisher;
    }

    /**
     * t_user start
     **/
    @Override
    public int addUserToDB(UserDTO userDTO) {
        return userRepository.add(userDTO);
    }

    @Override
    public UserDTO getUserByIdFromDB(long id) {
        return userRepository.get(id);
    }

    @Override
    public UserDTO getUserByUnionIdFromDB(String unionId) {
        return userRepository.getByUnionId(unionId);
    }

    @Override
    public UserDTO getUserByTokenFromDB(String unionId) {
        return userRepository.getByToken(unionId);
    }

    @Override
    public int updateUserToDB(UserDTO userDTO) {
        return userRepository.update(userDTO);
    }

    @Override
    public void checkUserOnlineServerId(int onlineServerId){
        userRepository.checkUserOnlineServerId(onlineServerId);
    }
    /** t_user end **/

    /**
     * t_user_attribute start
     **/
    @Override
    public int addUserAttributeToDB(UserAttributeDTO userAttributeDTO) {
        return userAttributeRepository.add(userAttributeDTO);
    }

    @Override
    public UserAttributeDTO getUserAttributeByIdFromDB(long userId) {
        return userAttributeRepository.get(userId);
    }
    /** t_user_attribute end **/

    /**
     * t_user_vehicle start
     **/
    @Override
    public int addUserVehicleToDB(UserVehicleDTO userVehicleDTO) {
        return userVehicleRepository.add(userVehicleDTO);
    }

    @Override
    public Map<Integer, UserVehicleDTO> getUserVehicleMapByIdFromDB(long userId) {
        return userVehicleRepository.getMap(userId);
    }
    /** t_user_vehicle end **/

    /**
     * t_user_vehicle_new start
     **/
    @Override
    public int addUserVehicleNewToDB(UserVehicleNewDTO userVehicleNewDTO) {
        return userVehicleNewRepository.add(userVehicleNewDTO);
    }

    @Override
    public Map<Integer, UserVehicleNewDTO> getUserVehicleNewMapByIdFromDB(long userId) {
        return userVehicleNewRepository.getMap(userId);
    }
    /** t_user_vehicle_new end **/

    /**
     * t_user_equipment start
     **/
    @Override
    public int addUserEquipmentToDB(UserEquipmentDTO userEquipmentDTO) {
        return userEquipmentRepository.add(userEquipmentDTO);
    }

    @Override
    public Map<Integer, UserEquipmentDTO> getUserEquipmentMapByIdFromDB(long userId) {
        return userEquipmentRepository.getMap(userId);
    }
    /** t_user_equipment end **/

    /**
     * t_user_buff_tool start
     **/
    @Override
    public int addUserBuffToolToDB(UserBuffToolDTO userBuffToolDTO) {
        return userBuffToolRepository.add(userBuffToolDTO);
    }

    @Override
    public Map<Integer, UserBuffToolDTO> getUserBuffToolMapByIdFromDB(long userId) {
        return userBuffToolRepository.getMap(userId);
    }
    /** t_user_buff_tool end **/

    /**
     * t_user_magnate start
     **/
    @Override
    public int addUserMagnateToDB(UserMagnateDTO userMagnateDTO) {
        return userMagnateRepository.add(userMagnateDTO);
    }

    @Override
    public Map<Integer, UserMagnateDTO> getUserMagnateMapByIdFromDB(long userId) {
        return userMagnateRepository.getMap(userId);
    }
    /** t_user_magnate end **/

    /**
     * t_user_boss start
     **/
    @Override
    public int addUserBossToDB(UserBossDTO userBossDTO) {
        return userBossRepository.add(userBossDTO);
    }

    @Override
    public Map<Integer, UserBossDTO> getUserBossMapByIdFromDB(long userId) {
        return userBossRepository.getMap(userId);
    }
    /** t_user_boss end **/

    /**
     * t_user_vip start
     **/
    @Override
    public int addUserVipToDB(UserVipDTO userVipDTO) {
        return userVipRepository.add(userVipDTO);
    }

    @Override
    public UserVipDTO getUserVipByIdFromDB(long userId) {
        return userVipRepository.get(userId);
    }

    /**
     * t_user_vip end
     **/

    @Async("asyncExecutor")
    public void asyncSaveDataOnLogoutEvent(long userId) {
        saveDataFromCacheToDB(userId,true);
    }

    @Override
    public void saveDataFromCacheToDB(long userId,boolean isRealSave) {
        try {
            Date currTime = new Date();
            /** save t_user **/
            UserDTO userDTO = UserManagerSingleton.getInstance().getUserByIdFromCache(userId);
            if (userDTO != null) {
                userDTO.setLatestLogoutTime(currTime);
                if(isRealSave){
                    userDTO.setOnlineServerId(0);
                }
                userDTO.setOnline(false);
                try{
                    userRepository.update(userDTO);
                }catch (Exception e){
                    if(isRealSave){
                        userRepository.updateOnlineServerIdById(userId,0);
                    }
                    userRepository.updateIsOnlineById(userId,false);
                    log.error("UserServiceImpl::saveDataFromCacheToDB:userId = {},userDTO = {},message = {},t_user数据保存失败", userId,userDTO,e.getMessage());
                }
                if(!isRealSave){
                    // 不是真正保存，保留用户数据在内存中
                    return;
                }
                UserManagerSingleton.getInstance().removeUserInCache(userId);
            }
            /** save t_user_attribute **/
            UserAttributeDTO userAttributeDTO = UserManagerSingleton.getInstance().getUserAttributeFromCache(userId);
            if (userAttributeDTO != null) {
                userAttributeRepository.update(userAttributeDTO);
                UserManagerSingleton.getInstance().removeUserAttributeInCache(userId);
            }
            /** save t_user_vehicle：已废弃 **/
            //Map<Integer, UserVehicleDTO> userVehicleDTOMap = UserManagerSingleton.getInstance().getUserVehicleMapByIdFromCache(userId);
            //if (userVehicleDTOMap != null) {
                //for (Map.Entry<Integer, UserVehicleDTO> entryVehicle : userVehicleDTOMap.entrySet()) {
                    //userVehicleRepository.update(entryVehicle.getValue());
                //}
                //UserManagerSingleton.getInstance().removeUserVehicleMapInCache(userId);
            //}
            /** save t_user_vehicle_new **/
            Map<Integer, UserVehicleNewDTO> userVehicleNewDTOMap = UserManagerSingleton.getInstance().getUserVehicleNewMapByIdFromCache(userId);
            if (userVehicleNewDTOMap != null) {
                for (Map.Entry<Integer, UserVehicleNewDTO> entryVehicleNew : userVehicleNewDTOMap.entrySet()) {
                    userVehicleNewRepository.update(entryVehicleNew.getValue());
                }
                UserManagerSingleton.getInstance().removeUserVehicleNewMapInCache(userId);
            }
            /** save t_user_equipment **/
            Map<Integer, UserEquipmentDTO> userEquipmentDTOMap = UserManagerSingleton.getInstance().getUserEquipmentMapByIdFromCache(userId);
            if (userEquipmentDTOMap != null) {
                for (Map.Entry<Integer, UserEquipmentDTO> entryEquipment : userEquipmentDTOMap.entrySet()) {
                    userEquipmentRepository.update(entryEquipment.getValue());
                }
                UserManagerSingleton.getInstance().removeUserEquipmentMapInCache(userId);
            }
            /** save t_user_buff_tool **/
            Map<Integer, UserBuffToolDTO> userBuffToolDTOMap = UserManagerSingleton.getInstance().getUserBuffToolMapByIdFromCache(userId);
            if (userBuffToolDTOMap != null) {
                for (Map.Entry<Integer, UserBuffToolDTO> entryBuffTool : userBuffToolDTOMap.entrySet()) {
                    userBuffToolRepository.update(entryBuffTool.getValue());
                }
                UserManagerSingleton.getInstance().removeUserBuffToolMapInCache(userId);
            }
            /** save t_user_magnate **/
            Map<Integer, UserMagnateDTO> userMagnateDTOMap = UserManagerSingleton.getInstance().getUserMagnateMapByIdFromCache(userId);
            if (userMagnateDTOMap != null) {
                for (Map.Entry<Integer, UserMagnateDTO> entryMagnate : userMagnateDTOMap.entrySet()) {
                    userMagnateRepository.update(entryMagnate.getValue());
                }
                UserManagerSingleton.getInstance().removeUserMagnateMapInCache(userId);
            }
            /** save t_user_boss **/
            Map<Integer, UserBossDTO> userBossDTOMap = UserManagerSingleton.getInstance().getUserBossMapByIdFromCache(userId);
            if (userBossDTOMap != null) {
                for (Map.Entry<Integer, UserBossDTO> entryBoss : userBossDTOMap.entrySet()) {
                    userBossRepository.update(entryBoss.getValue());
                }
                UserManagerSingleton.getInstance().removeUserBossMapInCache(userId);
            }
            /** save t_user_vip **/
            UserVipDTO userVipDTO = UserManagerSingleton.getInstance().getUserVipFromCache(userId);
            if (userVipDTO != null) {
                userVipRepository.update(userVipDTO);
                UserManagerSingleton.getInstance().removeUserVipInCache(userId);
            }
            log.info("UserServiceImpl::saveDataFromCacheToDB:userId = {},用户数据缓存至数据库保存成功", userId);
        } catch (Exception e) {
            log.error("UserServiceImpl::saveDataFromCacheToDB:userId = {},message = {},用户数据缓存至数据库保存失败", userId, e.getMessage());
        }
    }

    @Override
    public void checkSaveDataFromCacheToDB(int index){
        Date startTime = new Date();
        log.info("UserServiceImpl::checkSaveDataFromCacheToDB:index = {},startTime = {},定时检测保存角色数据开始",index,startTime);

        String lockKey = "checkSaveDataFromCacheToDB-" + CfgManagerSingleton.getInstance().getServerId();
        RLock lock = redissonClient.getLock(lockKey);
        int saveSuccessCount = 0;
        try{
            // 尝试获取锁，等待5秒，如果获取不到锁则放弃
            boolean locked = lock.tryLock(5, TimeUnit.SECONDS);
            if (locked) {
                Map<Long, UserDTO> userDTOMap = UserManagerSingleton.getInstance().getAllUserDTOMapFromCache();
                List<Long> userIdList = new ArrayList<>(userDTOMap.keySet());
                log.info("UserServiceImpl::checkSaveDataFromCacheToDB:index = {},定时检测保存角色数量 = {}", index,userIdList.size());

                for(Long userId : userIdList){
                    try{
                        UserDTO userDTO = userDTOMap.get(userId);
                        Date currTime = new Date();
                        long timeDiff = (currTime.getTime() - userDTO.getLatestLogoutTime().getTime())/1000L;
                        if(!userDTO.isOnline() && timeDiff > (1 * 60) ){
                            log.info("UserServiceImpl::checkSaveDataFromCacheToDB:index = {},userId = {},定时检测保存角色数据开始", index,userId);
                            // 当前不在线，并且离线已超5分钟，保存用户数据
                            saveDataFromCacheToDB(userDTO.getId(),true);
                            saveSuccessCount++;
                            log.info("UserServiceImpl::checkSaveDataFromCacheToDB:index = {},userId = {},定时检测保存角色数据结束",index,userId);
                        }
                    }catch (Exception e){
                        log.info("UserServiceImpl::checkSaveDataFromCacheToDB:index = {},userId = {},定时检测保存角色数据异常",index, userId);
                    }
                }
            } else {
                log.info("UserServiceImpl::checkSaveDataFromCacheToDB:index = {},获取分布式锁失败",index);
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            lock.unlock();
            log.info("UserServiceImpl::checkSaveDataFromCacheToDB:index = {},释放分布式锁",index);
        }

        Date endTime = new Date();
        log.info("UserServiceImpl::checkSaveDataFromCacheToDB:index = {},endTime = {},costTime = {}s,定时检测保存角色数据数量 = {},定时检测保存角色数据结束",
                index,endTime,(endTime.getTime()-startTime.getTime())/1000L,saveSuccessCount);
    }

    @Override
    public void saveDataFromCacheToDB() {
        Map<Long, UserDTO> userDTOMap = UserManagerSingleton.getInstance().getAllUserDTOMapFromCache();
        List<Long> userIdList = new ArrayList<>(userDTOMap.keySet());
        log.info("UserServiceImpl::saveDataFromCacheToDB:优雅关闭需保存数据角色数量 = {}", userDTOMap.size());

        int saveSuccessCount = 0;
        for (int i = 0; i < userIdList.size(); i++) {
            long userId = userIdList.get(i);

            log.info("UserServiceImpl::saveDataFromCacheToDB:userId = {},优雅关闭角色保存数据开始", userId);
            try {
                saveDataFromCacheToDB(userId,true);
            } catch (Exception e) {
                log.info("UserServiceImpl::saveDataFromCacheToDB:userId = {},优雅关闭角色保存数据异常", userId);
                continue;
            }
            log.info("UserServiceImpl::saveDataFromCacheToDB:userId = {},优雅关闭角色保存数据结束", userId);
            saveSuccessCount++;
        }

        log.info("UserServiceImpl::saveDataFromCacheToDB:优雅关闭已保存数据角色数量 = {}", saveSuccessCount);
    }

    @Override
    public BaseExecutor getExecutor(String executorName) {
        if (executorName.compareTo(UserCmdModule.settlementExecutorName) == 0) {
            return settlementExecutor;
        } else if (executorName.compareTo(UserCmdModule.attributeLevelUpExecutorName) == 0) {
            return attributeLevelUpExecutor;
        } else if (executorName.compareTo(UserCmdModule.changeVehicleExecutorName) == 0) {
            return changeVehicleExecutor;
        } else if (executorName.compareTo(UserCmdModule.useEquipmentExecutorName) == 0) {
            return useEquipmentExecutor;
        } else if (executorName.compareTo(UserCmdModule.startOrEndBuffToolToolExecutorName) == 0) {
            return startOrEndBuffToolExecutor;
        } else if (executorName.compareTo(UserCmdModule.getRedPacketExecutorName) == 0) {
            return getRedPacketExecutor;
        } else if (executorName.compareTo(UserCmdModule.challengeMagnateSuccessExecutorName) == 0) {
            return challengeMagnateSuccessExecutor;
        } else if (executorName.compareTo(UserCmdModule.challengeBossSuccessExecutorName) == 0) {
            return challengeBossSuccessExecutor;
        } else if (executorName.compareTo(UserCmdModule.unlockVehicleOrEquipmentExecutorName) == 0) {
            return unlockVehicleOrEquipmentExecutor;
        } else if (executorName.compareTo(UserCmdModule.logicHeartbeatExecutorName) == 0) {
            return logicHeartbeatExecutor;
        } else if (executorName.compareTo(UserCmdModule.gmCommandExecutorName) == 0) {
            return gmCommandExecutor;
        } else if (executorName.compareTo(UserCmdModule.watchedAdExecutorName) == 0) {
            return watchedAdExecutor;
        } else if (executorName.compareTo(UserCmdModule.selectStoneExecutorName) == 0) {
            return selectStoneExecutor;
        } else if (executorName.compareTo(UserCmdModule.sellStoneExecutorName) == 0) {
            return sellStoneExecutor;
        } else if (executorName.compareTo(UserCmdModule.addClickCountExecutorName) == 0) {
            return addClickCountExecutor;
        } else if (executorName.compareTo(UserCmdModule.vehicleNewLevelUpExecutorName) == 0) {
            return vehicleNewLevelUpExecutor;
        } else if (executorName.compareTo(UserCmdModule.lotteryTicketBuyExecutorName) == 0) {
            return lotteryTicketBuyExecutor;
        } else if (executorName.compareTo(UserCmdModule.lotteryTicketBonusGetExecutorName) == 0) {
            return lotteryTicketBonusGetExecutor;
        }

        return null;
    }
}

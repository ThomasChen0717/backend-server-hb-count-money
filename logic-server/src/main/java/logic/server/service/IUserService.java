package logic.server.service;

import logic.server.dto.UserAttributeDTO;
import logic.server.dto.UserBossDTO;
import logic.server.dto.UserBuffToolDTO;
import logic.server.dto.UserDTO;
import logic.server.dto.UserEquipmentDTO;
import logic.server.dto.UserMagnateDTO;
import logic.server.dto.UserVehicleDTO;
import logic.server.dto.UserVehicleNewDTO;
import logic.server.dto.UserVipDTO;
import logic.server.service.impl.action.BaseExecutor;

import java.util.Map;

/**
 * @author mark
 * @date 2023-04-14
 */
public interface IUserService {
    /**
     * t_user
     **/
    int addUserToDB(UserDTO userDTO);

    UserDTO getUserByIdFromDB(long userId);

    UserDTO getUserByUnionIdFromDB(String unionId);

    UserDTO getUserByTokenFromDB(String token);

    int updateUserToDB(UserDTO dto);
    void checkUserOnlineServerId(int onlineServerId);

    /**
     * t_user_attribute
     **/
    int addUserAttributeToDB(UserAttributeDTO userAttributeDTO);

    UserAttributeDTO getUserAttributeByIdFromDB(long userId);

    /**
     * t_user_vehicle
     **/
    int addUserVehicleToDB(UserVehicleDTO userVehicleDTO);

    Map<Integer, UserVehicleDTO> getUserVehicleMapByIdFromDB(long userId);

    /**
     * t_user_vehicle_new
     **/
    int addUserVehicleNewToDB(UserVehicleNewDTO userVehicleNewDTO);

    Map<Integer, UserVehicleNewDTO> getUserVehicleNewMapByIdFromDB(long userId);

    /**
     * t_user_equipment
     **/
    int addUserEquipmentToDB(UserEquipmentDTO userEquipmentDTO);

    Map<Integer, UserEquipmentDTO> getUserEquipmentMapByIdFromDB(long userId);

    /**
     * t_user_buff_tool
     **/
    int addUserBuffToolToDB(UserBuffToolDTO userBuffToolDTO);

    Map<Integer, UserBuffToolDTO> getUserBuffToolMapByIdFromDB(long userId);

    /**
     * t_user_magnate
     **/
    int addUserMagnateToDB(UserMagnateDTO userMagnateDTO);

    Map<Integer, UserMagnateDTO> getUserMagnateMapByIdFromDB(long userId);

    /**
     * t_user_boss
     **/
    int addUserBossToDB(UserBossDTO userBossDTO);

    Map<Integer, UserBossDTO> getUserBossMapByIdFromDB(long userId);

    /**
     * t_user_vip
     **/
    int addUserVipToDB(UserVipDTO userVipDTO);
    UserVipDTO getUserVipByIdFromDB(long userId);

    void asyncSaveDataOnLogoutEvent(long userId);// 异步保存用户数据
    void saveDataFromCacheToDB(long userId,boolean isRealSave);// 保存指定角色数据
    void checkSaveDataFromCacheToDB(int index);// 定时检测保存符合条件角色的数据
    void saveDataFromCacheToDB();// 保存所有角色数据

    void onlineUserCount();// 统计当前逻辑服用户数量
    /**
     * 获取指定执行器
     **/
    BaseExecutor getExecutor(String executorName);
}

package logic.server.service;

import logic.server.dto.UserAttributeDTO;
import logic.server.dto.UserBossDTO;
import logic.server.dto.UserBuffToolDTO;
import logic.server.dto.UserDTO;
import logic.server.dto.UserEquipmentDTO;
import logic.server.dto.UserMagnateDTO;
import logic.server.dto.UserVehicleDTO;
import logic.server.service.impl.action.BaseExecutor;

import java.util.Map;

/**
 * @author mark
 * @date 2023-04-14
 */
public interface IUserService {
    /** t_user **/
    int addUserToDB(UserDTO userDTO);
    UserDTO getUserByIdFromDB(long userId);
    UserDTO getUserByUnionIdFromDB(String unionId);
    UserDTO getUserByTokenFromDB(String token);
    int updateUserToDB(UserDTO dto);

    /** t_user_attribute **/
    int addUserAttributeToDB(UserAttributeDTO userAttributeDTO);
    UserAttributeDTO getUserAttributeByIdFromDB(long userId);

    /** t_user_vehicle **/
    int addUserVehicleToDB(UserVehicleDTO userVehicleDTO);
    Map<Integer,UserVehicleDTO> getUserVehicleMapByIdFromDB(long userId);

    /** t_user_equipment **/
    int addUserEquipmentToDB(UserEquipmentDTO userEquipmentDTO);
    Map<Integer,UserEquipmentDTO> getUserEquipmentMapByIdFromDB(long userId);

    /** t_user_buff_tool **/
    int addUserBuffToolToDB(UserBuffToolDTO userBuffToolDTO);
    Map<Integer,UserBuffToolDTO> getUserBuffToolMapByIdFromDB(long userId);

    /** t_user_magnate **/
    int addUserMagnateToDB(UserMagnateDTO userMagnateDTO);
    Map<Integer,UserMagnateDTO> getUserMagnateMapByIdFromDB(long userId);

    /** t_user_boss **/
    int addUserBossToDB(UserBossDTO userBossDTO);
    Map<Integer,UserBossDTO> getUserBossMapByIdFromDB(long userId);

    /** 角色下线数据处理 **/
    void saveDataFromCacheToDB(long userId);

    /** 获取指定执行器**/
    BaseExecutor getExecutor(String executorName);
}

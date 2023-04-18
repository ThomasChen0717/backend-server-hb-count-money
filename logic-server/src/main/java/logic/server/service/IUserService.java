package logic.server.service;

import logic.server.dto.UserAttributeDTO;
import logic.server.dto.UserDTO;
import logic.server.dto.UserVehicleDTO;

import java.util.Map;

/**
 * @author mark
 * @date 2023-04-14
 */
public interface IUserService {
    /** t_user **/
    int addUser(UserDTO userDTO);
    UserDTO getUserById(long userId);
    UserDTO getUserByUnionId(String unionId);
    UserDTO getUserByToken(String token);
    int updateUser(UserDTO dto);

    /** t_user_attribute **/
    int addUserAttribute(UserAttributeDTO userAttributeDTO);
    UserAttributeDTO getUserAttributeById(long userId);

    /** t_user_vehicle **/
    int addUserVehicle(UserVehicleDTO userVehicleDTO);
    Map<Integer,UserVehicleDTO> getUserVehicleMapById(long userId);
}

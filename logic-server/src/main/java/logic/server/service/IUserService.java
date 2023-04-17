package logic.server.service;

import logic.server.dto.UserDTO;

/**
 * @author mark
 * @date 2023-04-14
 */
public interface IUserService {
    int addUser(UserDTO userDTO);
    UserDTO getUserById(long userId);
    UserDTO getUserByUnionId(String unionId);
    UserDTO getUserByToken(String token);
    int updateUser(UserDTO dto);
}

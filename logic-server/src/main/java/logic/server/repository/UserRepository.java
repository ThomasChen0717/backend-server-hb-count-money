package logic.server.repository;

import logic.server.dto.UserDTO;

/**
 * @author mark
 * @date 2023-04-12
 */
public interface UserRepository {
    int add(UserDTO dto);

    int update(UserDTO dto);

    UserDTO get(long userId);
    UserDTO getByUnionId(String unionId);
    UserDTO getByToken(String token);
    void checkUserOnlineServerId(int onlineServerId);
    void updateOnlineServerIdById(long userId,int onlineServerId);
    void updateIsOnlineById(long userId,boolean isOnline);
}

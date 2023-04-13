package logic.server.repository;

import logic.server.dto.UsersDTO;

/**
 * @author mark
 * @date 2023-04-12
 */
public interface UsersRepository {
    Integer add(UsersDTO dto);
    UsersDTO get(Long userId);
    Integer update(UsersDTO dto);
}

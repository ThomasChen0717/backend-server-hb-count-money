package logic.server.repository;

import logic.server.dto.UserVipDTO;

public interface UserVipRepository {
    int add(UserVipDTO dto);
    int update(UserVipDTO dto);
    UserVipDTO get(long userId);
}

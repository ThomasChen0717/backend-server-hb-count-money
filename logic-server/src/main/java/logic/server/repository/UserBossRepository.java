package logic.server.repository;

import logic.server.dto.UserBossDTO;

import java.util.Map;

public interface UserBossRepository {
    int add(UserBossDTO dto);
    int update(UserBossDTO dto);
    Map<Integer, UserBossDTO> getMap(long userId);
    void deleteByUserId(long userId);
}

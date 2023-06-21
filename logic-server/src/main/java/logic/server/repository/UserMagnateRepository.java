package logic.server.repository;

import logic.server.dto.UserMagnateDTO;

import java.util.Map;

public interface UserMagnateRepository {
    int add(UserMagnateDTO dto);
    int update(UserMagnateDTO dto);
    Map<Integer, UserMagnateDTO> getMap(long userId);
    void deleteByUserId(long userId);
}

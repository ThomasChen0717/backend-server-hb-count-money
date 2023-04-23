package logic.server.repository;

import logic.server.dto.UserBuffToolDTO;

import java.util.Map;

public interface UserBuffToolRepository {
    int add(UserBuffToolDTO dto);
    int update(UserBuffToolDTO dto);
    Map<Integer, UserBuffToolDTO> getMap(long userId);
}

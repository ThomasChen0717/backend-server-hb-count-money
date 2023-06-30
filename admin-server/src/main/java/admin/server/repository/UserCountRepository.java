package admin.server.repository;

import admin.server.dto.UserCountDTO;

import java.util.Map;

public interface UserCountRepository {
    Map<Long, UserCountDTO> getMap();
}

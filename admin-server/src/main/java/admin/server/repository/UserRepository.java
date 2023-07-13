package admin.server.repository;

import admin.server.dto.UserDTO;

import java.util.Map;

public interface UserRepository {
    Map<Long, UserDTO> getMap();
}

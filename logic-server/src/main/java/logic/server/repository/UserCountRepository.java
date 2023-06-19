package logic.server.repository;

import logic.server.dto.UserCountDTO;

public interface UserCountRepository {
    int add(UserCountDTO dto);
}

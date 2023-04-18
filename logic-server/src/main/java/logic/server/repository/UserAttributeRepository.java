package logic.server.repository;

import logic.server.dto.UserAttributeDTO;

public interface UserAttributeRepository {
    int add(UserAttributeDTO dto);
    int update(UserAttributeDTO dto);
    UserAttributeDTO get(long userId);
}

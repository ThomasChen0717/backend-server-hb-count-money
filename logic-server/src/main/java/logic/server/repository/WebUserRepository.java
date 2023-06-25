package logic.server.repository;

import logic.server.dto.WebUserDTO;

import java.util.Map;

public interface WebUserRepository {
    Map<String, WebUserDTO> getMap();

    int add(WebUserDTO dto);
}

package admin.server.repository;

import admin.server.dto.WebUserDTO;

import java.util.Map;

public interface WebUserRepository {
    Map<String, WebUserDTO> getMap();

    Map<String, WebUserDTO> getMapByName();

    Map<Long, WebUserDTO> getMapById();

    int add(WebUserDTO dto);
    int update(WebUserDTO dto);
}

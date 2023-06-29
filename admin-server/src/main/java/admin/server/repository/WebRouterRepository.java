package admin.server.repository;

import admin.server.dto.WebRouterDTO;

import java.util.Map;

public interface WebRouterRepository {
    Map<String, WebRouterDTO> getMap();

    int add(WebRouterDTO dto);

    int update(WebRouterDTO dto);
}

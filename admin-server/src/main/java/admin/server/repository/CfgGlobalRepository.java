package admin.server.repository;

import admin.server.dto.CfgGlobalDTO;

import java.util.Map;

public interface CfgGlobalRepository {
    Map<String,CfgGlobalDTO> getMap();

    int delete();

    int add(CfgGlobalDTO dto);
}

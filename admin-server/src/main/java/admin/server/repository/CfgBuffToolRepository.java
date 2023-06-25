package admin.server.repository;

import admin.server.dto.CfgBuffToolDTO;

import java.util.Map;

public interface CfgBuffToolRepository {
    Map<Integer, CfgBuffToolDTO> getMap();

    int delete();

    int add(CfgBuffToolDTO dto);
}

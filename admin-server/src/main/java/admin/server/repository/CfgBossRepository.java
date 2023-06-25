package admin.server.repository;

import admin.server.dto.CfgBossDTO;

import java.util.Map;

public interface CfgBossRepository {
    Map<Integer, CfgBossDTO> getMap();

    int delete();

    int add(CfgBossDTO dto);
}

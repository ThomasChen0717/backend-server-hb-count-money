package admin.server.repository;

import admin.server.dto.CfgVipDTO;

import java.util.Map;

public interface CfgVipRepository {
    Map<Integer, CfgVipDTO> getMap();

    int delete();

    int add(CfgVipDTO dto);
}

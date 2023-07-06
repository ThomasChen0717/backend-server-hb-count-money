package admin.server.repository;

import admin.server.dto.CfgDrawDTO;

import java.util.Map;

public interface CfgDrawRepository {
    Map<Integer, CfgDrawDTO> getMap();

    int delete();

    int add(CfgDrawDTO dto);
}

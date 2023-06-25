package admin.server.repository;

import admin.server.dto.CfgMagnateDTO;

import java.util.Map;

public interface CfgMagnateRepository {
    Map<Integer, CfgMagnateDTO> getMap();

    int delete();

    int add(CfgMagnateDTO dto);
}

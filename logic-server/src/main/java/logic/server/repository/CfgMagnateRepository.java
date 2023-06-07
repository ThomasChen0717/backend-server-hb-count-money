package logic.server.repository;

import logic.server.dto.CfgBossDTO;
import logic.server.dto.CfgMagnateDTO;

import java.util.Map;

public interface CfgMagnateRepository {
    Map<Integer, CfgMagnateDTO> getMap();

    int delete();

    int add(CfgMagnateDTO dto);
}

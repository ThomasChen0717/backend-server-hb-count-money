package logic.server.repository;

import logic.server.dto.CfgBossDTO;
import logic.server.dto.CfgBuffToolDTO;

import java.util.Map;

public interface CfgBuffToolRepository {
    Map<Integer, CfgBuffToolDTO> getMap();

    int delete();

    int add(CfgBuffToolDTO dto);
}

package logic.server.repository;

import logic.server.dto.CfgAttributeDTO;
import logic.server.dto.CfgBossDTO;

import java.util.Map;

public interface CfgBossRepository {
    Map<Integer, CfgBossDTO> getMap();

    int delete();

    int add(CfgBossDTO dto);
}

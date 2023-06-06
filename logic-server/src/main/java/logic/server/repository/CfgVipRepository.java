package logic.server.repository;

import logic.server.dto.CfgBossDTO;
import logic.server.dto.CfgVipDTO;

import java.util.Map;

public interface CfgVipRepository {
    Map<Integer, CfgVipDTO> getMap();

    int delete();

    int add(CfgVipDTO dto);
}

package logic.server.repository;

import logic.server.dto.CfgBossDTO;

import java.util.Map;

public interface CfgBossRepository {
    Map<Integer, CfgBossDTO> getMap();
}

package logic.server.repository;

import logic.server.dto.CfgBossDTO;
import logic.server.dto.CfgGlobalDTO;

import java.util.Map;

public interface CfgGlobalRepository {
    Map<String,CfgGlobalDTO> getMap();

    int delete();

    int add(CfgGlobalDTO dto);
}

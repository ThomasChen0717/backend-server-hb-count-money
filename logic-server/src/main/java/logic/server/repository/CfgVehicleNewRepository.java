package logic.server.repository;

import logic.server.dto.CfgBossDTO;
import logic.server.dto.CfgVehicleNewDTO;

import java.util.Map;

public interface CfgVehicleNewRepository {
    Map<Integer, CfgVehicleNewDTO> getMap();

    int delete();

    int add(CfgVehicleNewDTO dto);
}

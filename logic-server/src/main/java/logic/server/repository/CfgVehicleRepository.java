package logic.server.repository;

import logic.server.dto.CfgBossDTO;
import logic.server.dto.CfgVehicleDTO;

import java.util.Map;

public interface CfgVehicleRepository {
    Map<Integer,CfgVehicleDTO> getMap();

    int delete();

    int add(CfgVehicleDTO dto);
}

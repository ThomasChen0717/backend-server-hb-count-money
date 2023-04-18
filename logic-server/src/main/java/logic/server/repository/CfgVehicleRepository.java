package logic.server.repository;

import logic.server.dto.CfgVehicleDTO;

import java.util.Map;

public interface CfgVehicleRepository {
    Map<Integer,CfgVehicleDTO> getMap();
}

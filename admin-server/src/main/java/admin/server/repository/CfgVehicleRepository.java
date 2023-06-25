package admin.server.repository;

import admin.server.dto.CfgVehicleDTO;

import java.util.Map;

public interface CfgVehicleRepository {
    Map<Integer,CfgVehicleDTO> getMap();

    int delete();

    int add(CfgVehicleDTO dto);
}

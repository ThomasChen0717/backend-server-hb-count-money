package admin.server.repository;

import admin.server.dto.CfgVehicleNewDTO;

import java.util.Map;

public interface CfgVehicleNewRepository {
    Map<Integer, CfgVehicleNewDTO> getMap();

    int delete();

    int add(CfgVehicleNewDTO dto);
}

package admin.server.repository;

import admin.server.dto.CfgEquipmentDTO;

import java.util.Map;

public interface CfgEquipmentRepository {
    Map<Integer, CfgEquipmentDTO> getMap();

    int delete();

    int add(CfgEquipmentDTO dto);
}

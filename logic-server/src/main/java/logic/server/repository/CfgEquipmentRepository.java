package logic.server.repository;

import logic.server.dto.CfgBossDTO;
import logic.server.dto.CfgEquipmentDTO;
import java.util.Map;

public interface CfgEquipmentRepository {
    Map<Integer, CfgEquipmentDTO> getMap();

    int delete();

    int add(CfgEquipmentDTO dto);
}

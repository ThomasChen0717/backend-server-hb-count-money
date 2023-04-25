package logic.server.service;

import logic.server.dto.CfgAttributeDTO;
import logic.server.dto.CfgBossDTO;
import logic.server.dto.CfgBuffToolDTO;
import logic.server.dto.CfgEquipmentDTO;
import logic.server.dto.CfgGlobalDTO;
import logic.server.dto.CfgMagnateDTO;
import logic.server.dto.CfgVehicleDTO;

import java.util.Map;

public interface ICfgService {
    Map<String, CfgGlobalDTO> getCfgGlobalMap();
    Map<Integer,CfgVehicleDTO> getCfgVehicleMap();
    Map<Integer, CfgAttributeDTO> getCfgAttributeMap();
    Map<Integer, CfgEquipmentDTO> getCfgEquipmentMap();
    Map<Integer, CfgBuffToolDTO> getCfgBuffToolMap();
    Map<Integer, CfgMagnateDTO> getCfgMagnateMap();
    Map<Integer, CfgBossDTO> getCfgBossMap();
}

package logic.server.service;

import logic.server.dto.CfgAttributeDTO;
import logic.server.dto.CfgGlobalDTO;
import logic.server.dto.CfgVehicleDTO;

import java.util.Map;

public interface ICfgService {
    Map<String, CfgGlobalDTO> getCfgGlobalMap();
    Map<Integer,CfgVehicleDTO> getCfgVehicleMap();
    Map<Integer, CfgAttributeDTO> getCfgAttributeMap();
}

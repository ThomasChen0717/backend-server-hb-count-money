package logic.server.singleton;

import logic.server.dto.CfgAttributeDTO;
import logic.server.dto.CfgEquipmentDTO;
import logic.server.dto.CfgGlobalDTO;
import logic.server.dto.CfgVehicleDTO;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Data
@Slf4j
@Service
public class CfgManagerSingleton {
    private static CfgManagerSingleton instance;
    private String serverId = "1";
    private Map<String,CfgGlobalDTO> cfgGlobalDTOMap;
    private Map<Integer,CfgVehicleDTO> cfgVehicleDTOMap;
    private Map<Integer, CfgAttributeDTO> cfgAttributeDTOMap;
    private Map<Integer, CfgEquipmentDTO> cfgEquipmentDTOMap;

    public static synchronized CfgManagerSingleton getInstance() {
        if (instance == null) {
            instance = new CfgManagerSingleton();
        }
        return instance;
    }
    public CfgManagerSingleton() {
        cfgGlobalDTOMap = new HashMap<>();
        cfgVehicleDTOMap = new HashMap<>();
        cfgAttributeDTOMap = new HashMap<>();
        cfgEquipmentDTOMap = new HashMap<>();
    }

    public CfgGlobalDTO getCfgGlobalByKeyFromCache(String key){
        return cfgGlobalDTOMap.get(key);
    }
    public CfgVehicleDTO getCfgVehicleByTypeFromCache(int vehicleType){
        return cfgVehicleDTOMap.get(vehicleType);
    }
    public CfgAttributeDTO getCfgAttributeByTypeFromCache(int attributeType){
        return cfgAttributeDTOMap.get(attributeType);
    }
    public CfgEquipmentDTO getCfgEquipmentByIdFromCache(int equipmentId){
        return cfgEquipmentDTOMap.get(equipmentId);
    }
}

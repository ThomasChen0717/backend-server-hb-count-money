package logic.server.singleton;

import logic.server.dto.CfgAttributeDTO;
import logic.server.dto.CfgGlobalDTO;
import logic.server.dto.CfgVehicleDTO;
import logic.server.service.ICfgService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Map;

@Data
@Slf4j
@Service
public class CfgManagerSingleton {
    @Autowired
    private ICfgService cfgService;
    private static CfgManagerSingleton instance;
    private Map<String,CfgGlobalDTO> cfgGlobalDTOMap;
    private Map<Integer,CfgVehicleDTO> cfgVehicleDTOMap;
    private Map<Integer, CfgAttributeDTO> cfgAttributeDTOMap;

    public static synchronized CfgManagerSingleton getInstance() {
        if (instance == null) {
            instance = new CfgManagerSingleton();
        }
        return instance;
    }
    private CfgManagerSingleton() {
        cfgGlobalDTOMap = cfgService.getCfgGlobalMap();
        cfgVehicleDTOMap = cfgService.getCfgVehicleMap();
        cfgAttributeDTOMap = cfgService.getCfgAttributeMap();
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
}

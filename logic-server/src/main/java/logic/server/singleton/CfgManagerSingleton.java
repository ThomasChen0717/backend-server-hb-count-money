package logic.server.singleton;

import logic.server.dto.CfgAttributeDTO;
import logic.server.dto.CfgBossDTO;
import logic.server.dto.CfgBuffToolDTO;
import logic.server.dto.CfgEquipmentDTO;
import logic.server.dto.CfgGlobalDTO;
import logic.server.dto.CfgMagnateDTO;
import logic.server.dto.CfgVehicleDTO;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.ognl.IntHashMap;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Data
@Slf4j
@Service
public class CfgManagerSingleton {
    private static CfgManagerSingleton instance;
    private String serverId = null;
    private Map<String,CfgGlobalDTO> cfgGlobalDTOMap;
    private Map<Integer,CfgVehicleDTO> cfgVehicleDTOMap;
    private Map<Integer, CfgAttributeDTO> cfgAttributeDTOMap;
    private Map<Integer, CfgEquipmentDTO> cfgEquipmentDTOMap;
    private Map<Integer, CfgBuffToolDTO> cfgBuffToolDTOMap;
    private Map<Integer, CfgMagnateDTO> cfgMagnateDTOMap;
    private Map<Integer, CfgBossDTO> cfgBossDTOMap;

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
        cfgBuffToolDTOMap = new HashMap<>();
        cfgMagnateDTOMap = new HashMap<>();
        cfgBossDTOMap = new IntHashMap();
    }

    public void setServerId(String serverIdFromNacos){
        serverId = serverIdFromNacos;
        log.info("CfgManagerSingleton::setServerId:serverIdFromNacos = {}",serverIdFromNacos);
    }

    /** t_cfg_global **/
    public CfgGlobalDTO getCfgGlobalByKeyFromCache(String key){
        return cfgGlobalDTOMap.get(key);
    }

    /** t_cfg_vehicle **/
    public CfgVehicleDTO getCfgVehicleByTypeFromCache(int vehicleType){
        return cfgVehicleDTOMap.get(vehicleType);
    }

    /** t_cfg_attribute **/
    public CfgAttributeDTO getCfgAttributeByTypeFromCache(int attributeType){
        return cfgAttributeDTOMap.get(attributeType);
    }

    /** t_cfg_equipment **/
    public CfgEquipmentDTO getCfgEquipmentByIdFromCache(int equipmentId){
        return cfgEquipmentDTOMap.get(equipmentId);
    }

    /** t_cfg_buff_tool **/
    public CfgBuffToolDTO getCfgBuffToolByIdFromCache(int buffToolId){
        return cfgBuffToolDTOMap.get(buffToolId);
    }

    /** t_cfg_magnate **/
    public CfgMagnateDTO getCfgMagnateByIdFromCache(int magnateId){
        return cfgMagnateDTOMap.get(magnateId);
    }
    public CfgMagnateDTO getCfgMagnateByPreMagnateIdFromCache(int preMagnateId){
        for(Map.Entry<Integer,CfgMagnateDTO> entry : cfgMagnateDTOMap.entrySet()){
            if(entry.getValue().getPreMagnateId() == preMagnateId){
                return entry.getValue();
            }
        }
        return null;
    }

    /** t_cfg_boss **/
    public CfgBossDTO getCfgBossByIdFromCache(int bossId){
        return cfgBossDTOMap.get(bossId);
    }
    public CfgBossDTO getCfgBossByPreBossIdFromCache(int preBossId){
        for(Map.Entry<Integer,CfgBossDTO> entry : cfgBossDTOMap.entrySet()){
            if(entry.getValue().getPreBossId() == preBossId){
                return entry.getValue();
            }
        }
        return null;
    }
}

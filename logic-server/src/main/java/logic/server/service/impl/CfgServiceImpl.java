package logic.server.service.impl;

import logic.server.config.NacosConfiguration;
import logic.server.dto.CfgAttributeDTO;
import logic.server.dto.CfgBossDTO;
import logic.server.dto.CfgBuffToolDTO;
import logic.server.dto.CfgEquipmentDTO;
import logic.server.dto.CfgGlobalDTO;
import logic.server.dto.CfgMagnateDTO;
import logic.server.dto.CfgVehicleDTO;
import logic.server.repository.CfgAttributeRepository;
import logic.server.repository.CfgBossRepository;
import logic.server.repository.CfgBuffToolRepository;
import logic.server.repository.CfgEquipmentRepository;
import logic.server.repository.CfgGlobalRepository;
import logic.server.repository.CfgMagnateRepository;
import logic.server.repository.CfgVehicleRepository;
import logic.server.service.ICfgService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Map;

@Slf4j
@Service
public class CfgServiceImpl implements ICfgService {
    @Autowired
    private CfgGlobalRepository cfgGlobalRepository;
    @Autowired
    private CfgVehicleRepository cfgVehicleRepository;
    @Autowired
    private CfgAttributeRepository cfgAttributeRepository;
    @Autowired
    private CfgEquipmentRepository cfgEquipmentRepository;
    @Autowired
    private CfgBuffToolRepository cfgBuffToolRepository;
    @Autowired
    private CfgMagnateRepository cfgMagnateRepository;
    @Autowired
    private CfgBossRepository cfgBossRepository;

    /** nacos **/
    public String getServerId(){
        String loginServerId = "";
        return loginServerId;
    }

    /** t_cfg_global **/
    @Override
    public Map<String, CfgGlobalDTO> getCfgGlobalMap(){
        return cfgGlobalRepository.getMap();
    }

    /** t_cfg_vehicle **/
    @Override
    public Map<Integer,CfgVehicleDTO> getCfgVehicleMap(){
        return cfgVehicleRepository.getMap();
    }

    /** t_cfg_attribute **/
    @Override
    public Map<Integer, CfgAttributeDTO> getCfgAttributeMap(){
        return cfgAttributeRepository.getMap();
    }

    /** t_cfg_equipment **/
    @Override
    public Map<Integer, CfgEquipmentDTO> getCfgEquipmentMap(){
        return cfgEquipmentRepository.getMap();
    }

    /** t_cfg_buff_tool **/
    @Override
    public Map<Integer, CfgBuffToolDTO> getCfgBuffToolMap(){
        return cfgBuffToolRepository.getMap();
    }

    /** t_cfg_magnate **/
    @Override
    public Map<Integer, CfgMagnateDTO> getCfgMagnateMap(){
        return cfgMagnateRepository.getMap();
    }

    /** t_cfg_boss **/
    @Override
    public Map<Integer, CfgBossDTO> getCfgBossMap(){
        return cfgBossRepository.getMap();
    }
}

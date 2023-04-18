package logic.server.service.impl;

import logic.server.dto.CfgAttributeDTO;
import logic.server.dto.CfgGlobalDTO;
import logic.server.dto.CfgVehicleDTO;
import logic.server.repository.CfgAttributeRepository;
import logic.server.repository.CfgGlobalRepository;
import logic.server.repository.CfgVehicleRepository;
import logic.server.service.ICfgService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
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

    @Override
    public Map<String, CfgGlobalDTO> getCfgGlobalMap(){
        return cfgGlobalRepository.getMap();
    }
    @Override
    public Map<Integer,CfgVehicleDTO> getCfgVehicleMap(){
        return cfgVehicleRepository.getMap();
    }
    @Override
    public Map<Integer, CfgAttributeDTO> getCfgAttributeMap(){
        return cfgAttributeRepository.getMap();
    }
}

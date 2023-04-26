package logic.server.init;

import groovy.util.logging.Slf4j;
import logic.server.service.ICfgService;
import logic.server.singleton.CfgManagerSingleton;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class DataInit implements CommandLineRunner {
    @Autowired
    private ICfgService cfgService;

    @Override
    public void run(String... args) throws Exception {
        // 初始化一些数据
        CfgManagerSingleton.getInstance().setCfgGlobalDTOMap(cfgService.getCfgGlobalMap());
        CfgManagerSingleton.getInstance().setCfgVehicleDTOMap(cfgService.getCfgVehicleMap());
        CfgManagerSingleton.getInstance().setCfgAttributeDTOMap(cfgService.getCfgAttributeMap());
        CfgManagerSingleton.getInstance().setCfgEquipmentDTOMap(cfgService.getCfgEquipmentMap());
        CfgManagerSingleton.getInstance().setCfgBuffToolDTOMap(cfgService.getCfgBuffToolMap());
        CfgManagerSingleton.getInstance().setCfgMagnateDTOMap(cfgService.getCfgMagnateMap());
        CfgManagerSingleton.getInstance().setCfgBossDTOMap(cfgService.getCfgBossMap());
        CfgManagerSingleton.getInstance().setServerId(cfgService.getServerId());
    }
}

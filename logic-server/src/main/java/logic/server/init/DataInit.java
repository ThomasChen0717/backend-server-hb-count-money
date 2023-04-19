package logic.server.init;

import logic.server.service.ICfgService;
import logic.server.singleton.CfgManagerSingleton;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

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
    }
}

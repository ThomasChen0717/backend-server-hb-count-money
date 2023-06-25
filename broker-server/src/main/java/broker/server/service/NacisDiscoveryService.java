package broker.server.service;

import broker.server.config.NacosConfiguration;
import com.alibaba.fastjson2.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class NacisDiscoveryService {
    @Autowired
    private DiscoveryClient discoveryClient;

    @Autowired
    private NacosConfiguration nacosConfiguration;

    public List<JSONObject> getServerList(){
        List<JSONObject> jsonServerList = new ArrayList<>();

        List<String> serverIdList = discoveryClient.getServices();
        List<ServiceInstance> instanceList = discoveryClient.getInstances("hb-countmoney-broker-server");
        if (instanceList.isEmpty()) {
            return jsonServerList;
        }
        for(ServiceInstance serviceInstance : instanceList){
            JSONObject jsonService = new JSONObject();
            jsonService.put("host",serviceInstance.getHost());
            jsonService.put("port",serviceInstance.getPort());
            jsonServerList.add(jsonService);
        }

        log.info("NacisDiscoveryService::getServerList:jsonServerList = {}",jsonServerList);
        return jsonServerList;
    }

    public boolean isDevEnv(){
        return nacosConfiguration.getSpringProfilesActive().compareTo("dev") == 0 ? true : false;
    }
}

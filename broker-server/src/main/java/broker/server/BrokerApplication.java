package broker.server;

import broker.server.service.NacisDiscoveryService;
import broker.server.util.BeanUtils;
import com.alibaba.fastjson2.JSONObject;
import com.iohao.game.action.skeleton.ext.spring.ActionFactoryBeanForSpring;
import com.iohao.game.bolt.broker.core.common.BrokerGlobalConfig;
import com.iohao.game.bolt.broker.server.BrokerServer;
import com.iohao.game.simple.cluster.ClusterSimpleHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;
import java.util.List;

/**
 * 单独启动类：游戏网关
 *
 * @author mark
 * @date 2023-04-10
 */
@SpringBootApplication
@EnableDiscoveryClient
@Slf4j
public class BrokerApplication {
    public static void main(String[] args) {

        // 启动 spring boot
        SpringApplication.run(BrokerApplication.class, args);

        /** 单个网关服 **/
        //singleModeStart();

        /** 集群方式网关服 **/
        clusterModeStart();
    }

    /** 单个网关服 **/
    private static void singleModeStart(){
        // broker （游戏网关）
        BrokerServer brokerServer = new BrokerBoot().createBrokerServer();
        // 启动游戏网关
        brokerServer.startup();
    }

    /** 集群方式网关服 **/
    private static void clusterModeStart(){
        /*
         * 种子节点地址
         * <pre>
         *     格式： ip:port
         *
         *     -- 生产环境的建议 --
         *     注意，在生产上建议一台物理机配置一个 broker （游戏网关）
         *     一个 broker 就是一个节点
         *     比如配置三台机器，端口可以使用同样的端口，假设三台机器的 ip 分别是:
         *     192.168.1.10:30056
         *     192.168.1.11:30056
         *     192.168.1.12:30056
         *
         *     -- 为了方便演示 --
         *     这里配置写死是方便在一台机器上启动集群
         *     但是同一台机器启动多个 broker 来实现集群就要使用不同的端口，因为《端口被占用，不能相同》
         *     所以这里的配置是：
         *     127.0.0.1:30056
         *     127.0.0.1:30057
         *     127.0.0.1:30058
         * </pre>
         */

        // nacos服务发现模式无法使用，因为服务每次启动ip都会动态变化
        boolean isNacosDiscovery = false;
        int clusterPort = 30056;
        List<String> seedAddress = new ArrayList<>();
        if(isNacosDiscovery){
            NacisDiscoveryService nacisDiscoveryService = BeanUtils.getBean(NacisDiscoveryService.class);
            List<JSONObject> serverList = nacisDiscoveryService.getServerList();
            for(JSONObject jsonServer : serverList){
                String hostAndPort = jsonServer.getString("host") + ":" + clusterPort;
                seedAddress.add(hostAndPort);
            }
        }else{
            NacisDiscoveryService nacisDiscoveryService = BeanUtils.getBean(NacisDiscoveryService.class);
            if(nacisDiscoveryService.isDevEnv()){
                String hostAndPort = "127.0.0.1" + ":" + clusterPort;
                seedAddress.add(hostAndPort);
            }else{
                // 第1台网关域名:countmoney-broker-server-0.countmoney-broker-server-hs:10200
                // 第2台网关域名:countmoney-broker-server-1.countmoney-broker-server-hs:10200
                // 第3台网关域名:countmoney-broker-server-2.countmoney-broker-server-hs:10200
                String hostAndPort = "countmoney-broker-server-0.countmoney-broker-server-hs" + ":" + clusterPort;
                seedAddress.add(hostAndPort);
            }
        }

        /*
         * 第 1 台集群 游戏网关: 【集群端口 - 30056】、【游戏网关端口 - 10200】
         * 因为是在同一台机器上测试游戏网关集群，同一台机器启动多个 broker 来实现集群就要使用不同的端口，因为《端口被占用，不能相同》
         */
        int[] gossipPortAndBrokerPort = new int[]{clusterPort, BrokerGlobalConfig.brokerPort};

        // Gossip listen port 监听端口
        int gossipListenPort = gossipPortAndBrokerPort[0];
        // broker 端口（游戏网关端口）
        int port = gossipPortAndBrokerPort[1];
        // ---- 第1台 broker ----
        log.info("BrokerApplication::clusterModeStart:seedAddress = {},gossipListenPort = {},port = {}",seedAddress,gossipListenPort,port);
        BrokerServer brokerServer = ClusterSimpleHelper.createBrokerServer(seedAddress, gossipListenPort, port);
        // 启动游戏网关
        brokerServer.startup();
    }

    @Bean
    public ActionFactoryBeanForSpring actionFactoryBean() {
        // 将业务框架交给 spring 管理
        return ActionFactoryBeanForSpring.me();
    }
}

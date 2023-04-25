package broker.server;

import com.iohao.game.bolt.broker.core.common.BrokerGlobalConfig;
import com.iohao.game.bolt.broker.server.BrokerServer;
import com.iohao.game.simple.cluster.ClusterSimpleHelper;

import java.util.List;

/**
 * 单独启动类：游戏网关
 *
 * @author mark
 * @date 2023-04-10
 */
public class BrokerApplication {
    public static void main(String[] args) {

        /** 单个网关服 **/
        // broker （游戏网关）
        //BrokerServer brokerServer = new BrokerBoot().createBrokerServer();
        // 启动游戏网关
        //brokerServer.startup();

        /** 集群方式网关服 **/
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
        List<String> seedAddress = List.of(
                "127.0.0.1:30056"
        );

        /*
         * 第 1 台集群 游戏网关: 【集群端口 - 30056】、【游戏网关端口 - 10200】
         * 因为是在同一台机器上测试游戏网关集群，同一台机器启动多个 broker 来实现集群就要使用不同的端口，因为《端口被占用，不能相同》
         */
        int[] gossipPortAndBrokerPort = new int[]{30056, BrokerGlobalConfig.brokerPort};

        // Gossip listen port 监听端口
        int gossipListenPort = gossipPortAndBrokerPort[0];
        // broker 端口（游戏网关端口）
        int port = gossipPortAndBrokerPort[1];
        // ---- 第1台 broker ----
        BrokerServer brokerServer = ClusterSimpleHelper.createBrokerServer(seedAddress, gossipListenPort, port);
        // 启动游戏网关
        brokerServer.startup();
    }
}

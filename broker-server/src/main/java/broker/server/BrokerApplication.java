package broker.server;

import com.iohao.game.bolt.broker.server.BrokerServer;

/**
 * 单独启动类：游戏网关
 *
 * @author mark
 * @date 2023-04-10
 */
public class BrokerApplication {
    public static void main(String[] args) {

        // broker （游戏网关）
        BrokerServer brokerServer = new BrokerBoot().createBrokerServer();

        // 启动游戏网关
        brokerServer.startup();
    }
}

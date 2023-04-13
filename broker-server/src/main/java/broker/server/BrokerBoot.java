package broker.server;

import com.iohao.game.bolt.broker.core.common.BrokerGlobalConfig;
import com.iohao.game.bolt.broker.server.BrokerServer;
import com.iohao.game.bolt.broker.server.BrokerServerBuilder;

/**
 * @author mark
 * @date 2023-04-10
 */
public class BrokerBoot {
    public BrokerServer createBrokerServer() {
        // broker （游戏网关） 构建器
        BrokerServerBuilder brokerServerBuilder = BrokerServer.newBuilder()
                // broker （游戏网关）默认端口 10200
                .port(BrokerGlobalConfig.brokerPort)
                ;
        return brokerServerBuilder.build();
    }
}

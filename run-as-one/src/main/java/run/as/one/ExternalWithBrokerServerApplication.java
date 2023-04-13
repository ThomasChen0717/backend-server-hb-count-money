package run.as.one;

import broker.server.BrokerBoot;
import com.iohao.game.bolt.broker.client.external.ExternalServer;
import com.iohao.game.bolt.broker.client.external.config.ExternalGlobalConfig;
import com.iohao.game.bolt.broker.server.BrokerServer;
import com.iohao.game.simple.SimpleRunOne;
import external.server.ExternalBoot;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author mark
 * @date 2023-04-10
 */
@Slf4j
@SpringBootApplication
public class ExternalWithBrokerServerApplication {
    public static void main(String[] args) {

        // 启动 spring boot
        SpringApplication.run(ExternalWithBrokerServerApplication.class, args);

        // 对外开放的端口
        int externalPort = 10100;
        // 游戏对外服
        ExternalServer externalServer = new ExternalBoot().createExternalServer(externalPort);

        // broker （游戏网关）
        BrokerServer brokerServer = new BrokerBoot().createBrokerServer();

        // 多服单进程的方式部署（类似单体应用）
        new SimpleRunOne()
                // broker （游戏网关）
                .setBrokerServer(brokerServer)
                // 游戏对外服
                .setExternalServer(externalServer)
                // 启动 游戏对外服、游戏网关、游戏逻辑服
                .startup();

        // 先关闭访问验证
        ExternalGlobalConfig.accessAuthenticationHook.setVerifyIdentity(false);
    }
}

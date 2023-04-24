package external.server;

import com.iohao.game.action.skeleton.ext.spring.ActionFactoryBeanForSpring;
import com.iohao.game.bolt.broker.client.external.ExternalServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * 单独启动类：游戏对外服
 *
 * @author mark
 * @date 2023-04-07
 */
@SpringBootApplication
public class ExternalApplication {

    public static void main(String[] args) {

        // 启动 spring boot
        SpringApplication.run(ExternalApplication.class, args);

        // 对外开放的端口
        int externalPort = 10100;

        // 构建游戏对外服
        ExternalServer externalServer = new ExternalBoot().createExternalServer(externalPort);

        // 启动游戏对外服
        externalServer.startup();
    }

    @Bean
    public ActionFactoryBeanForSpring actionFactoryBean() {
        // 将业务框架交给 spring 管理
        return ActionFactoryBeanForSpring.me();
    }
}

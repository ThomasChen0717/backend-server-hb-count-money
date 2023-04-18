package logic.server;

import com.iohao.game.action.skeleton.ext.spring.ActionFactoryBeanForSpring;
import com.iohao.game.bolt.broker.client.BrokerClientApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * 单独启动类：游戏逻辑服
 *
 * @author mark
 * @date 2023-04-09
 */
@SpringBootApplication
public class LogicApplication {
    public static void main(String[] args) {

        // 启动 spring boot
        SpringApplication.run(LogicApplication.class, args);

        // 启动业务逻辑服
        LogicClient logicClient = new LogicClient();
        BrokerClientApplication.start(logicClient);
    }

    @Bean
    public ActionFactoryBeanForSpring actionFactoryBean() {
        // 将业务框架交给 spring 管理
        return ActionFactoryBeanForSpring.me();
    }
}

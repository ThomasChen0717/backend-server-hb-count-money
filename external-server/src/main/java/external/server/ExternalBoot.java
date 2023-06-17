package external.server;

import com.iohao.game.bolt.broker.client.external.ExternalServer;
import com.iohao.game.bolt.broker.client.external.ExternalServerBuilder;
import com.iohao.game.bolt.broker.client.external.bootstrap.ExternalJoinEnum;
import com.iohao.game.bolt.broker.client.external.bootstrap.heart.IdleProcessSetting;
import com.iohao.game.bolt.broker.client.external.config.ExternalGlobalConfig;
import com.iohao.game.bolt.broker.client.external.session.UserSessions;
import com.iohao.game.bolt.broker.core.client.BrokerAddress;
import com.iohao.game.bolt.broker.core.common.IoGameGlobalConfig;
import common.pb.cmd.LoginCmdModule;
import external.server.config.ConfigTemplate;
import external.server.hook.ClientIdleHook;
import external.server.hook.ClientUserHook;
import external.server.util.BeanUtils;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

/**
 * @author mark
 * @date 2023-04-07
 */
public class ExternalBoot {
    private ConfigTemplate configTemplate = BeanUtils.getBean(ConfigTemplate.class);

    public ExternalServer createExternalServer(int externalPort) {

        extractedIgnore();

        // 设置 自定义 用户上线、下线的钩子
        UserSessions.me().setUserHook(new ClientUserHook());

        // 心跳相关设置
        IdleProcessSetting idleProcessSetting = new IdleProcessSetting()
                .readerIdleTime(40L)
                .writerIdleTime(40L)
                .allIdleTime(40L)
                // 设置 自定义心跳钩子事件回调
                .idleHook(new ClientIdleHook());

        String brokerServerUrl = configTemplate.getBrokerServerUrl();
        int brokerServerPort = configTemplate.getBrokerServerPort();

        // 游戏对外服 - 构建器
        ExternalServerBuilder builder = ExternalServer.newBuilder(externalPort)
                // websocket 方式连接
                .externalJoinEnum(ExternalJoinEnum.WEBSOCKET)
                // Broker （游戏网关）的连接地址；如果不设置，默认也是这个配置
                .brokerAddress(new BrokerAddress(brokerServerUrl, brokerServerPort))
                // 开启心跳
                //.enableIdle(idleProcessSetting)
                ;

        // 构建游戏对外服
        return builder.build();
    }

    private void extractedIgnore() {
        /*
         * 注意，权限相关验证配置在游戏对外服是正确的，因为是游戏对外服在控制访问验证
         * see https://www.yuque.com/iohao/game/tywkqv#qEvtB
         */
        var accessAuthenticationHook = ExternalGlobalConfig.accessAuthenticationHook;
        // 表示登录才能访问业务方法
        accessAuthenticationHook.setVerifyIdentity(true);
        /*
         * 由于 accessAuthenticationHook.verifyIdentity = true; 时，需要玩家登录才可以访问业务方法 （action）
         *
         * 在这可以配置一些忽略访问限制的路由。
         * 这里配置的路由，表示不登录也可以进行访问
         * 现在忽略的 1-1，是登录 action 的路由，所以当我们访问 1-1 路由时，可以不登录。
         * 忽略的路由可以添加多个。
         */
        // 登录接口（1-1）忽略控制访问验证
        accessAuthenticationHook.addIgnoreAuthenticationCmd(LoginCmdModule.cmd, LoginCmdModule.loginVerify);
    }
}

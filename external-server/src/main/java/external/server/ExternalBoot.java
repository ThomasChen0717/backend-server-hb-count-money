package external.server;

import com.iohao.game.bolt.broker.client.external.ExternalServer;
import com.iohao.game.bolt.broker.client.external.ExternalServerBuilder;
import com.iohao.game.bolt.broker.client.external.bootstrap.ExternalJoinEnum;
import com.iohao.game.bolt.broker.client.external.bootstrap.heart.IdleProcessSetting;
import com.iohao.game.bolt.broker.client.external.config.ExternalGlobalConfig;
import com.iohao.game.bolt.broker.client.external.session.UserSessions;
import com.iohao.game.bolt.broker.core.client.BrokerAddress;
import com.iohao.game.bolt.broker.core.common.IoGameGlobalConfig;
import external.server.hook.ClientIdleHook;
import external.server.hook.ClientUserHook;

/**
 * @author mark
 * @date 2023-04-07
 */
public class ExternalBoot {

    public ExternalServer createExternalServer(int externalPort) {

        extractedIgnore();

        // 设置 自定义 用户上线、下线的钩子
        UserSessions.me().setUserHook(new ClientUserHook());

        // 心跳相关设置
        IdleProcessSetting idleProcessSetting = new IdleProcessSetting()
                .readerIdleTime(15L)
                .writerIdleTime(15L)
                .allIdleTime(30L)
                // 设置 自定义心跳钩子事件回调
                .idleHook(new ClientIdleHook());

        // 游戏对外服 - 构建器
        ExternalServerBuilder builder = ExternalServer.newBuilder(externalPort)
                // websocket 方式连接
                .externalJoinEnum(ExternalJoinEnum.WEBSOCKET)
                // Broker （游戏网关）的连接地址；如果不设置，默认也是这个配置
                .brokerAddress(new BrokerAddress("127.0.0.1", IoGameGlobalConfig.brokerPort))
                // 开启心跳
                .enableIdle(idleProcessSetting)
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
        accessAuthenticationHook.addIgnoreAuthenticationCmd(1, 1);
    }
}

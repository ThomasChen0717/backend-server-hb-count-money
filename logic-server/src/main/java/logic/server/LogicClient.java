package logic.server;

import com.iohao.game.action.skeleton.core.doc.BarSkeletonDoc;
import com.iohao.game.bolt.broker.core.client.BrokerAddress;
import com.iohao.game.bolt.broker.core.common.BrokerGlobalConfig;
import com.iohao.game.common.kit.NetworkKit;
import logic.server.action.LoginAction;
import logic.server.parent.logic.core.MyBarSkeletonConfig;
import com.iohao.game.action.skeleton.core.BarSkeleton;
import com.iohao.game.action.skeleton.core.BarSkeletonBuilder;
import com.iohao.game.action.skeleton.core.BarSkeletonBuilderParamConfig;
import com.iohao.game.bolt.broker.client.AbstractBrokerClientStartup;
import com.iohao.game.bolt.broker.core.client.BrokerClient;
import com.iohao.game.bolt.broker.core.client.BrokerClientBuilder;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

/**
 * 游戏逻辑服
 *
 * @author mark
 * @date 2023-04-09
 */
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LogicClient extends AbstractBrokerClientStartup {
    @Override
    public BarSkeleton createBarSkeleton() {
        // 业务框架构建器 配置
        BarSkeletonBuilderParamConfig config = MyBarSkeletonConfig.createBarSkeletonBuilderParamConfig()
                // 扫描 action 类所在包
                .scanActionPackage(LoginAction.class);

        // 业务框架构建器
        BarSkeletonBuilder builder = MyBarSkeletonConfig.createBarSkeletonBuilder(config);
        // 开启 jsr380 验证
        builder.getSetting().setValidator(true);

        BarSkeleton barSkeleton = builder.build();

        // 生成游戏文档
        BarSkeletonDoc.me().buildDoc();

        return barSkeleton;
    }

    @Override
    public BrokerClientBuilder createBrokerClientBuilder() {
        BrokerClientBuilder builder = BrokerClient.newBuilder();
        builder.appName("业务游戏逻辑服");
        return builder;
    }

    @Override
    public BrokerAddress createBrokerAddress() {
        // 类似 127.0.0.1 ，但这里是本机的 ip
        String localIp = NetworkKit.LOCAL_IP;
        // broker （游戏网关）默认端口
        int brokerPort = BrokerGlobalConfig.brokerPort;
        return new BrokerAddress(localIp, brokerPort);
    }
}

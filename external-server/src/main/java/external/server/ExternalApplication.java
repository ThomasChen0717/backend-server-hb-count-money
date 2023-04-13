package external.server;

import com.iohao.game.bolt.broker.client.external.ExternalServer;

/**
 * 单独启动类：游戏对外服
 *
 * @author mark
 * @date 2023-04-07
 */
public class ExternalApplication {

    public static void main(String[] args) {

        // 对外开放的端口
        int externalPort = 10100;

        // 构建游戏对外服
        ExternalServer externalServer = new ExternalBoot().createExternalServer(externalPort);

        // 启动游戏对外服
        externalServer.startup();
    }
}

package com.baida.countmoney.client.command;

import com.iohao.game.action.skeleton.core.CmdKit;
import com.iohao.game.action.skeleton.core.DataCodecKit;
import com.iohao.game.bolt.broker.client.external.bootstrap.message.ExternalMessage;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.nio.ByteBuffer;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author mark
 * @date 2023-04-09
 */
@Slf4j
//@UtilityClass
public class WebsocketClientKit {
    public WebSocketClient runClient(ClientCommandKit clientCommandKit) throws Exception {
        // 连接游戏服务器的地址
        String wsUrl = "ws://127.0.0.1:10100/websocket";
        //String wsUrl = "wss://hb-games-external-test.leyonb.com:443/websocket";
        //String wsUrl = "ws://192.168.20.4:10100/websocket";

        WebSocketClient webSocketClient = new WebSocketClient(new URI(wsUrl), new Draft_6455()) {
            @Override
            public void onOpen(ServerHandshake handshakedata) {
                // 连续多次发送请求命令到游戏服务器
                sendMessage();
            }

            @Override
            public void onMessage(String s) {
            }

            @Override
            public void onClose(int i, String s, boolean b) {
            }

            @Override
            public void onError(Exception e) {
            }

            @Override
            public void onMessage(ByteBuffer byteBuffer) {
                // 接收服务器返回的消息
                clientCommandKit.printOnMessage(byteBuffer);

                // 继续发送
                sendMessage();
            }

            public void sendMessage(){
                clientCommandKit.listClientCommand().forEach(clientCommand -> {
                    ExternalMessage externalMessage = clientCommand.getExternalMessage();
                    int cmdMerge = externalMessage.getCmdMerge();
                    int cmd = CmdKit.getCmd(cmdMerge);
                    int subCmd = CmdKit.getSubCmd(cmdMerge);

                    byte[] bytes = DataCodecKit.encode(externalMessage);
                    this.send(bytes);

                    log.info("WebSocketClient::sendMessage:robotIndex = {},userId = {},发送请求 {}-{}",clientCommandKit.robotIndex,clientCommandKit.userId, cmd, subCmd);
                    long sleepMilliseconds = clientCommand.sleepMilliseconds;
                    // 执行完请求后，进行睡眠的时间
                    if (sleepMilliseconds > 0) {
                        try {
                            TimeUnit.MILLISECONDS.sleep(sleepMilliseconds);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                 });
            }
        };

        // 开始连接服务器
        webSocketClient.connect();

        return webSocketClient;
    }
}


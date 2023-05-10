package com.baida.countmoney.client.command;

import com.alipay.remoting.rpc.HeartbeatCommand;
import com.iohao.game.action.skeleton.core.CmdKit;
import com.iohao.game.action.skeleton.core.DataCodecKit;
import com.iohao.game.bolt.broker.client.external.bootstrap.ExternalKit;
import com.iohao.game.bolt.broker.client.external.bootstrap.message.ExternalMessage;
import lombok.Data;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.nio.ByteBuffer;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * @author mark
 * @date 2023-04-09
 */
@Slf4j
//@UtilityClass
public class ClientCommandKit {

    Map<Integer, ClientCommand> clientCommandMap = new LinkedHashMap<>();
    Map<Integer, ClientCommand> clientCommandMapForRobot = new LinkedHashMap<>();

    public List<ClientCommand> listClientCommand() {
        return clientCommandMap
                .values()
                .stream()
                .toList();
    }

    public ClientCommand createClientCommand(ExternalMessage externalMessage, Class<?> resultClass, long sleepMilliseconds) {
        ClientCommand clientCommand = new ClientCommand();
        clientCommand.externalMessage = externalMessage;
        clientCommand.resultClass = resultClass;
        clientCommand.sleepMilliseconds = sleepMilliseconds;

        clientCommandMap.put(externalMessage.getCmdMerge(), clientCommand);

        return clientCommand;
    }

    public ClientCommand createClientCommandForRobot(ExternalMessage externalMessage, Class<?> resultClass, long sleepMilliseconds) {
        ClientCommand clientCommand = new ClientCommand();
        clientCommand.externalMessage = externalMessage;
        clientCommand.resultClass = resultClass;
        clientCommand.sleepMilliseconds = sleepMilliseconds;

        clientCommandMapForRobot.put(externalMessage.getCmdMerge(), clientCommand);

        return clientCommand;
    }

    public ClientCommand createClientCommand(ExternalMessage externalMessage, Class<?> resultClass) {
        return createClientCommand(externalMessage, resultClass, 0);
    }

    public ClientCommand createClientCommandForRobot(ExternalMessage externalMessage, Class<?> resultClass) {
        return createClientCommandForRobot(externalMessage, resultClass, 0);
    }

    public ClientCommand createClientCommand(ExternalMessage externalMessage, long sleepMilliseconds) {
        return createClientCommand(externalMessage, null, sleepMilliseconds);
    }

    public ClientCommand createClientCommand(ExternalMessage externalMessage) {
        return createClientCommand(externalMessage, null);
    }

    public ExternalMessage createExternalMessage(int cmd, int subCmd) {
        return createExternalMessage(cmd, subCmd, null);
    }

    public ExternalMessage createExternalMessage(int cmd, int subCmd, Object object) {
        // 游戏框架内置的协议， 与游戏前端相互通讯的协议
        return ExternalKit.createExternalMessage(cmd, subCmd, object);
    }

    public void addParseResult(int cmd, int subCmd, Class<?> resultClass) {
        ClientCommand clientCommand = new ClientCommand();
        clientCommand.resultClass = resultClass;

        int mergeCmd = CmdKit.merge(cmd, subCmd);
        clientCommandMap.put(mergeCmd, clientCommand);
    }

    public ClientCommand getClientCommandForRobot(){
        if(clientCommandMapForRobot.size() == 0) return null;
        Random r = new Random();
        int randomNumber = r.nextInt(clientCommandMapForRobot.size());
        int count = 0;
        for(Map.Entry<Integer, ClientCommand> entry : clientCommandMapForRobot.entrySet()){
            if(count == randomNumber){
                return entry.getValue();
            }
            count++;
        }
        return null;
    }

    public void printOnMessage(ByteBuffer byteBuffer) {
        System.out.println();
        // 接收服务器返回的消息
        byte[] dataContent = byteBuffer.array();
        ExternalMessage externalMessage = DataCodecKit.decode(dataContent, ExternalMessage.class);
        int cmdMerge = externalMessage.getCmdMerge();
        int cmd = CmdKit.getCmd(cmdMerge);
        int subCmd = CmdKit.getSubCmd(cmdMerge);

        log.info("ClientCommandKit::printOnMessage:ExternalMessage = {},cmdMerge = [{}-{}],收到消息",
                externalMessage, cmd, subCmd);

        if (externalMessage.getResponseStatus() == 0) {
            printNormal(externalMessage);
        } else {
            printError(externalMessage);
        }

        if(clientCommandMap.get(cmdMerge) != null) {
            clientCommandMap.remove(cmdMerge);
            ClientCommand clientCommand = getClientCommandForRobot();
            if(clientCommand != null)
                clientCommandMap.put(clientCommand.getExternalMessage().getCmdMerge(), clientCommand);
        }
    }

    private void printError(ExternalMessage message) {
        int responseStatus = message.getResponseStatus();
        String validMsg = message.getValidMsg();

        log.info("ClientCommandKit::printOnMessage:错误码 = {} 错误消息 = {}", responseStatus, validMsg);
    }

    private void printNormal(ExternalMessage message) {
        int cmdMerge = message.getCmdMerge();
        int cmd = CmdKit.getCmd(cmdMerge);
        int subCmd = CmdKit.getSubCmd(cmdMerge);

        ClientCommand clientCommand = clientCommandMap.get(cmdMerge);

        if (clientCommand == null) {
            log.info("ClientCommandKit::printOnMessage:cmdMerge = {},cmd = {},subCmd = {},没有对应的处理类",cmdMerge,cmd,subCmd);
            return;
        }

        if (clientCommand.resultClass == null) {
            log.info("ClientCommandKit::printOnMessage:cmdMerge = {},cmd = {},subCmd = {},没有对应的处理类来反序列化结果, resultClass is null",cmdMerge,cmd,subCmd);
            return;
        }

        byte[] data = message.getData();
        Object o = DataCodecKit.decode(data, clientCommand.resultClass);

        log.info("ClientCommandKit::printOnMessage:o = {}", o);
    }
}

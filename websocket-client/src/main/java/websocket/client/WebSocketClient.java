package websocket.client;

import com.alibaba.fastjson.JSONObject;
import com.baida.countmoney.client.command.ClientCommandKit;
import com.baida.countmoney.client.command.WebsocketClientKit;
import com.iohao.game.bolt.broker.client.external.bootstrap.message.ExternalMessage;
import common.pb.cmd.LoginCmdModule;
import common.pb.cmd.UserCmdModule;
import common.pb.pb.ChallengeBossSuccessReqPb;
import common.pb.pb.ChallengeBossSuccessResPb;
import common.pb.pb.GmCommandReqPb;
import common.pb.pb.GmCommandResPb;
import common.pb.pb.LogicHeartbeatReqPb;
import common.pb.pb.LogicHeartbeatResPb;
import common.pb.pb.LoginReqPb;
import common.pb.pb.LoginResPb;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 游戏客户端模拟启动类
 *
 * @author mark
 * @date 2023-04-09
 */
@Slf4j
public class WebSocketClient {

    public static void main(String[] args) throws Exception {
        // 启动客户端测试
        List<String> tokenList = new ArrayList<>();
        tokenList.add("3d9abe07-2c5c-46bb-8047-10d25f42e02b");
        //tokenList.add("19a78629-6884-486e-9cb4-6fbb3ee96df3");
        int createRobotCount = 0;
        for(int i=0;i<createRobotCount;i++){
            tokenList.add("");
        }
        for(String token : tokenList){
            startWebsocketClient(token);
        }
    }

    private static void startWebsocketClient(String token) throws Exception{
        ClientCommandKit clientCommandKit = new ClientCommandKit();
        WebsocketClientKit websocketClientKit = new WebsocketClientKit();

        // 请求构建 - 登录相关
        initLoginCommand(token,clientCommandKit);

        // 元信息相关
        attachmentCommands();

        TimeUnit.MILLISECONDS.sleep(5);

        // 请求构建
        initClientCommands(clientCommandKit);
//
//        // 逻辑服间的相互通信
//        communicationClientCommands();
//
//        // 其他
//        otherCommand();

        // 启动客户端
        websocketClientKit.runClient(clientCommandKit);
    }

    private static void attachmentCommands() {
        /**
        // 请求 设置元信息
        ExternalMessage externalMessage = ClientCommandKit.createExternalMessage(
                HallCmdModule.cmd,
                HallCmdModule.attachment
        );

        ClientCommandKit.createClientCommand(externalMessage, 1000);

        // 请求 打印元信息
        externalMessage = ClientCommandKit.createExternalMessage(
                HallCmdModule.cmd,
                HallCmdModule.attachmentPrint
        );

        ClientCommandKit.createClientCommand(externalMessage, MyAttachment.class);
         **/
    }

    private static void otherCommand() {
        /**
        // 请求、响应
        ExternalMessage externalMessageHelloRoom = ClientCommandKit.createExternalMessage(
                RoomCmdModule.cmd,
                RoomCmdModule.helloRoom
        );

        ClientCommandKit.createClientCommand(externalMessageHelloRoom, OtherVerify.class);


        ExternalMessage externalMessageLongValueWithBroadcast = ClientCommandKit.createExternalMessage(
                OtherSchoolCmdModule.cmd,
                OtherSchoolCmdModule.longValueWithBroadcast
        );

        ClientCommandKit.createClientCommand(externalMessageLongValueWithBroadcast, UserInfo.class);
        ClientCommandKit.addParseResult(OtherSchoolCmdModule.cmd, OtherSchoolCmdModule.longValueWithBroadcastData, SchoolPb.class);
         **/
    }

    private static void initLoginCommand(String token,ClientCommandKit clientCommandKit) {
        /*
         *       注意，这个业务码放这里只是为了方便测试多种情况
         *       交由测试请求端来控制
         *
         *       ------------业务码说明------------------
         *       当业务码为 【0】时 (相当于号已经在线上了，不能重复登录)
         *       如果游戏对外服已经有该玩家的连接了，就抛异常，告诉请求端玩家已经在线。
         *       否则就正常登录成功
         *
         *       当业务码为 【1】时 （相当于顶号）
         *       强制断开之前的客户端连接，并让本次登录成功。
         */

        int loginBizCode = 0;

        // 登录请求
        LoginReqPb loginReqPb = new LoginReqPb();
        loginReqPb.setLoginPlatform("hb");
        if(token == null || token.isEmpty()){
            // 创建新用户
            loginReqPb.setCode("LKBkYy3y0LDlmM9sC0720005fad8b71e9ad1");
        }else {
            // 登录老用户
            //loginReqPb.setToken("3d9abe07-2c5c-46bb-8047-10d25f42e02b");
            loginReqPb.setToken(token);
        }

        ExternalMessage externalMessageLogin = clientCommandKit.createExternalMessage(
                LoginCmdModule.cmd,
                LoginCmdModule.loginVerify,
                loginReqPb
        );

        clientCommandKit.createClientCommand(externalMessageLogin, LoginResPb.class, 6000);
    }

    private static void initClientCommands(ClientCommandKit clientCommandKit) {
        //if(true){
            //return;
        //}

        LogicHeartbeatReqPb logicHeartbeatReqPb = new LogicHeartbeatReqPb();

        // 请求、响应
        ExternalMessage externalMessageHere = clientCommandKit.createExternalMessage(
                UserCmdModule.cmd,
                UserCmdModule.logicHeartbeat,
                logicHeartbeatReqPb
        );

        clientCommandKit.createClientCommandForRobot(externalMessageHere, LogicHeartbeatResPb.class,10000);


        /**
        // 请求、无响应
        ExternalMessage externalMessageHereVoid = ClientCommandKit.createExternalMessage(
                LoginCmdModule.cmd,
                LoginCmdModule.hereVoid,
                logicRequestPb
        );

        ClientCommandKit.createClientCommand(externalMessageHereVoid, LogicRequestPb.class);


        // 更新学校信息，jsr380
        SchoolPb schoolPb = new SchoolPb();
        schoolPb.email = "ioGame@game.com";
        schoolPb.classCapacity = 99;
        schoolPb.teacherNum = 40;
        schoolPb.teacherNum = 70;

        ExternalMessage externalMessageJSR380 = ClientCommandKit.createExternalMessage(
                LoginCmdModule.cmd,
                LoginCmdModule.jsr380,
                schoolPb
        );

        ClientCommandKit.createClientCommand(externalMessageJSR380);

        //SchoolPb name=null 在加入分组校验时 该用例会返回校验失败: name不能为null
        ExternalMessage externalMessageGroup = ClientCommandKit.createExternalMessage(
                LoginCmdModule.cmd,
                LoginCmdModule.group,
                schoolPb
        );

        ClientCommandKit.createClientCommand(externalMessageGroup);


        //断言 + 异常机制 = 清晰简洁的代码
        SchoolLevelPb schoolLevelPb = new SchoolLevelPb();
        schoolLevelPb.level = 11;
        schoolLevelPb.vipLevel = 10;

        ExternalMessage externalMessageAssert = ClientCommandKit.createExternalMessage(
                LoginCmdModule.cmd,
                LoginCmdModule.assertWithException,
                schoolLevelPb
        );

        ClientCommandKit.createClientCommand(externalMessageAssert);


        // 广播相关
        ExternalMessage externalMessageBroadcast = ClientCommandKit.createExternalMessage(
                LoginCmdModule.cmd,
                LoginCmdModule.broadcast
        );

        ClientCommandKit.createClientCommand(externalMessageBroadcast);

        // 添加接收广播的解析
        ClientCommandKit.addParseResult(
                LoginCmdModule.cmd,
                LoginCmdModule.broadcastData,
                SpringBroadcastMessagePb.class
        );

        // 业务参数自动装箱、拆箱基础类型，解决碎片协议问题
        IntValue intValue = new IntValue();
        intValue.value = 10;

        ExternalMessage externalMessageIntValueWrapper = ClientCommandKit.createExternalMessage(
                LoginCmdModule.cmd,
                LoginCmdModule.intValueWrapper,
                intValue
        );

        ClientCommandKit.createClientCommand(externalMessageIntValueWrapper, IntValue.class);


        OtherVerify otherVerify = new OtherVerify();
        otherVerify.jwt = "j";

        // 请求、响应
        ExternalMessage externalMessageJsr380 = ClientCommandKit.createExternalMessage(
                OtherSchoolCmdModule.cmd,
                OtherSchoolCmdModule.jsr380,
                otherVerify
        );

        ClientCommandKit.createClientCommand(externalMessageJsr380);
         **/
    }

    private static void communicationClientCommands() {
        /**
        // 3.1 单个逻辑服与单个逻辑服通信请求 - 有返回值（可跨进程）
        ExternalMessage externalMessageCommunication31 = ClientCommandKit.createExternalMessage(
                SchoolCmdModule.cmd,
                SchoolCmdModule.communication31
        );

        ClientCommandKit.createClientCommand(externalMessageCommunication31);


        // 3.2 单个逻辑服与单个逻辑服通信请求 - 无返回值（可跨进程）
        ExternalMessage externalMessageCommunication32 = ClientCommandKit.createExternalMessage(
                SchoolCmdModule.cmd,
                SchoolCmdModule.communication32
        );

        ClientCommandKit.createClientCommand(externalMessageCommunication32);

        // 3.3 单个逻辑服与同类型多个逻辑服通信请求（可跨进程）

        ExternalMessage externalMessageCommunication33 = ClientCommandKit.createExternalMessage(
                SchoolCmdModule.cmd,
                SchoolCmdModule.communication33
        );

        ClientCommandKit.createClientCommand(externalMessageCommunication33);
         **/
    }
}


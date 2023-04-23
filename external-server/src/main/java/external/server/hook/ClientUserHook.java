package external.server.hook;

import com.alipay.remoting.exception.RemotingException;
import com.iohao.game.action.skeleton.core.CmdKit;
import com.iohao.game.action.skeleton.protocol.RequestMessage;
import com.iohao.game.bolt.broker.client.external.bootstrap.ExternalKit;
import com.iohao.game.bolt.broker.client.external.session.UserSession;
import com.iohao.game.bolt.broker.client.external.session.UserSessions;
import com.iohao.game.bolt.broker.client.external.session.hook.UserHook;
import common.pb.cmd.LoginCmdModule;
import lombok.extern.slf4j.Slf4j;

/**
 * @author mark
 * @date 2023-04-15
 */
@Slf4j
public class ClientUserHook implements UserHook {
    @Override
    public void into(UserSession userSession) {
        long userId = userSession.getUserId();
        log.info("ClientUserHook::into:userId = {},channelId = {},用户上线", userId, userSession.getUserChannelId());
        log.info("ClientUserHook::into:当前在线玩家数量 = {}", UserSessions.me().countOnline());
    }

    @Override
    public void quit(UserSession userSession) {
        long userId = userSession.getUserId();
        log.info("ClientUserHook::quit:userId = {},channelId = {},用户下线", userId, userSession.getUserChannelId());
        log.info("ClientUserHook::quit:当前在线玩家数量 = {}", UserSessions.me().countOnline());

        // 向逻辑服发送用户下线消息
        int mergeCmd = CmdKit.merge(LoginCmdModule.cmd, LoginCmdModule.logout);
        // 创建请求消息，createRequestMessage 有多个重载，可以传入业务参数
        RequestMessage requestMessage = ExternalKit.createRequestMessage(mergeCmd);

        try {
            // 请求游戏网关
            // 由内部逻辑服转发用户请求到游戏网关，在由网关转到具体的业务逻辑服
            ExternalKit.requestGateway(userSession, requestMessage);
            log.info("ClientUserHook::quit:userId = {},向逻辑服发送用户下线消息成功", userId);
        } catch (RemotingException e) {
            e.printStackTrace();
            log.error("ClientUserHook::quit:userId = {},向逻辑服发送用户下线消息失败", userId);
        }
    }
}

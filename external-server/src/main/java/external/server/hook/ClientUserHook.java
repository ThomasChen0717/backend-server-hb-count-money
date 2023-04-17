package external.server.hook;

import com.alipay.remoting.exception.RemotingException;
import com.iohao.game.action.skeleton.core.CmdKit;
import com.iohao.game.action.skeleton.protocol.RequestMessage;
import com.iohao.game.bolt.broker.client.external.bootstrap.ExternalKit;
import com.iohao.game.bolt.broker.client.external.session.UserSession;
import com.iohao.game.bolt.broker.client.external.session.UserSessions;
import com.iohao.game.bolt.broker.client.external.session.hook.UserHook;
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
    }
}

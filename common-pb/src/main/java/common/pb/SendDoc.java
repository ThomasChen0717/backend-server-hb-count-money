package common.pb;

import com.iohao.game.action.skeleton.annotation.DocActionSend;
import com.iohao.game.action.skeleton.annotation.DocActionSends;
import common.pb.cmd.LoginCmdModule;

/**
 * 广播（推送）文档生成标记
 *
 * @author mark
 * @date 2023-04-10
 */
@DocActionSends({
        @DocActionSend(cmd = LoginCmdModule.cmd,
                subCmd = LoginCmdModule.broadcastData,
                dataClass = BroadcastMessagePb.class),
})
public class SendDoc {
}
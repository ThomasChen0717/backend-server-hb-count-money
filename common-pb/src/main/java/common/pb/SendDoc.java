package common.pb;

import com.iohao.game.action.skeleton.annotation.DocActionSend;
import com.iohao.game.action.skeleton.annotation.DocActionSends;
import common.pb.cmd.LoginCmdModule;
import common.pb.cmd.UserCmdModule;
import common.pb.pb.MoneySyncPushPb;

/**
 * 广播（推送）文档生成标记
 *
 * @author mark
 * @date 2023-04-10
 */
@DocActionSends({
        @DocActionSend(cmd = UserCmdModule.cmd,
                subCmd = UserCmdModule.moneySyncPush,
                dataClass = MoneySyncPushPb.class),
})
public class SendDoc {
}
package common.pb;

import com.iohao.game.action.skeleton.annotation.DocActionSend;
import com.iohao.game.action.skeleton.annotation.DocActionSends;
import common.pb.cmd.UserCmdModule;
import common.pb.pb.MoneySyncPushPb;
import common.pb.pb.TitleSyncPushPb;

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
        @DocActionSend(cmd = UserCmdModule.cmd,
                subCmd = UserCmdModule.titleSyncPush,
                dataClass = TitleSyncPushPb.class),
        @DocActionSend(cmd = UserCmdModule.cmd,
                subCmd = UserCmdModule.vipSyncPush,
                dataClass = TitleSyncPushPb.class),
})
public class SendDoc {
}
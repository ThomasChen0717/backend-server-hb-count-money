package logic.server.service.impl;

import com.iohao.game.action.skeleton.core.CmdInfo;
import com.iohao.game.action.skeleton.core.commumication.BroadcastContext;
import com.iohao.game.bolt.broker.core.client.BrokerClientHelper;
import common.pb.cmd.UserCmdModule;
import common.pb.pb.MoneySyncPushPb;
import common.pb.pb.TitleSyncPushPb;
import logic.server.dto.UserDTO;
import logic.server.service.IPushPbService;
import logic.server.singleton.UserManagerSingleton;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class PushPbServiceImpl implements IPushPbService {
    @Override
    public void moneySync(long userId){
        MoneySyncPushPb moneySyncPushPb = new MoneySyncPushPb();
        UserDTO userDTO = UserManagerSingleton.getInstance().getUserByIdFromCache(userId);
        moneySyncPushPb.setMoney(userDTO.getMoney()).setMoneyHistory(userDTO.getMoneyHistory());
        BroadcastContext broadcastContext = BrokerClientHelper.getBroadcastContext();
        CmdInfo cmdInfo = CmdInfo.getCmdInfo(UserCmdModule.cmd,UserCmdModule.moneySyncPush);
        broadcastContext.broadcast(cmdInfo, moneySyncPushPb,userId);
    }

    @Override
    public void titleSync(long userId){
        TitleSyncPushPb titleSyncPushPb = new TitleSyncPushPb();
        UserDTO userDTO = UserManagerSingleton.getInstance().getUserByIdFromCache(userId);
        titleSyncPushPb.setTitle(userDTO.getTitle());
        BroadcastContext broadcastContext = BrokerClientHelper.getBroadcastContext();
        CmdInfo cmdInfo = CmdInfo.getCmdInfo(UserCmdModule.cmd,UserCmdModule.titleSyncPush);
        broadcastContext.broadcast(cmdInfo, titleSyncPushPb,userId);
    }
}

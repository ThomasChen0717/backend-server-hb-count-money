package logic.server.action;

import com.iohao.game.action.skeleton.annotation.ActionController;
import com.iohao.game.action.skeleton.annotation.ActionMethod;
import com.iohao.game.action.skeleton.core.exception.MsgException;
import com.iohao.game.action.skeleton.protocol.HeadMetadata;
import com.iohao.game.action.skeleton.protocol.RequestMessage;
import common.pb.cmd.UserCmdModule;
import common.pb.pb.AddClickCountReqPb;
import common.pb.pb.AddClickCountResPb;
import common.pb.pb.AttributeLevelUpReqPb;
import common.pb.pb.AttributeLevelUpResPb;
import common.pb.pb.DrawBuffOpenReqPb;
import common.pb.pb.DrawBuffOpenResPb;
import common.pb.pb.DrawReqPb;
import common.pb.pb.DrawResPb;
import common.pb.pb.DrawRoundReadyReqPb;
import common.pb.pb.DrawRoundReadyResPb;
import common.pb.pb.DrawSettlementReqPb;
import common.pb.pb.DrawSettlementResPb;
import common.pb.pb.DrawUseBagReqPb;
import common.pb.pb.DrawUseBagResPb;
import common.pb.pb.LotteryTicketBonusGetReqPb;
import common.pb.pb.LotteryTicketBonusGetResPb;
import common.pb.pb.LotteryTicketBuyReqPb;
import common.pb.pb.LotteryTicketBuyResPb;
import common.pb.pb.ChallengeBossSuccessReqPb;
import common.pb.pb.ChallengeBossSuccessResPb;
import common.pb.pb.ChallengeMagnateSuccessReqPb;
import common.pb.pb.ChallengeMagnateSuccessResPb;
import common.pb.pb.ChangeVehicleReqPb;
import common.pb.pb.ChangeVehicleResPb;
import common.pb.pb.GetRedPacketReqPb;
import common.pb.pb.GetRedPacketResPb;
import common.pb.pb.GmCommandReqPb;
import common.pb.pb.GmCommandResPb;
import common.pb.pb.LogicHeartbeatReqPb;
import common.pb.pb.LogicHeartbeatResPb;
import common.pb.pb.SelectStoneReqPb;
import common.pb.pb.SelectStoneResPb;
import common.pb.pb.SellStoneReqPb;
import common.pb.pb.SellStoneResPb;
import common.pb.pb.SettlementReqPb;
import common.pb.pb.SettlementResPb;
import common.pb.pb.StartOrEndBuffToolReqPb;
import common.pb.pb.StartOrEndBuffToolResPb;
import common.pb.pb.UnlockVehicleOrEquipmentReqPb;
import common.pb.pb.UnlockVehicleOrEquipmentResPb;
import common.pb.pb.UseEquipmentReqPb;
import common.pb.pb.UseEquipmentResPb;
import common.pb.pb.UserDataFromCacheToDBReqPb;
import common.pb.pb.UserDataFromCacheToDBResPb;
import common.pb.pb.VehicleNewLevelUpReqPb;
import common.pb.pb.VehicleNewLevelUpResPb;
import common.pb.pb.WatchedAdReqPb;
import common.pb.pb.WatchedAdResPb;
import logic.server.parent.action.skeleton.core.flow.MyFlowContext;
import logic.server.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author mark
 * @date 2023-04-20
 */
@Slf4j
@Component
@ActionController(UserCmdModule.cmd)
public class UserAction {
    @Autowired
    private IUserService userService;

    @ActionMethod(UserCmdModule.settlement)
    public SettlementResPb settlement(SettlementReqPb settlementReqPb, MyFlowContext myFlowContext) throws MsgException{
        return (SettlementResPb)userService.getExecutor(UserCmdModule.settlementExecutorName).executor(settlementReqPb,myFlowContext.getUserId());
    }

    @ActionMethod(UserCmdModule.attributeLevelUp)
    public AttributeLevelUpResPb attributeLevelUp(AttributeLevelUpReqPb attributeLevelUpReqPb, MyFlowContext myFlowContext) throws MsgException{
        return (AttributeLevelUpResPb)userService.getExecutor(UserCmdModule.attributeLevelUpExecutorName).executor(attributeLevelUpReqPb,myFlowContext.getUserId());
    }

    @ActionMethod(UserCmdModule.changeVehicle)
    public ChangeVehicleResPb changeVehicle(ChangeVehicleReqPb changeVehicleReqPb, MyFlowContext myFlowContext) throws MsgException{
        return (ChangeVehicleResPb)userService.getExecutor(UserCmdModule.changeVehicleExecutorName).executor(changeVehicleReqPb,myFlowContext.getUserId());
    }

    @ActionMethod(UserCmdModule.useEquipment)
    public UseEquipmentResPb useEquipment(UseEquipmentReqPb useEquipmentReqPb, MyFlowContext myFlowContext) throws MsgException{
        return (UseEquipmentResPb)userService.getExecutor(UserCmdModule.useEquipmentExecutorName).executor(useEquipmentReqPb,myFlowContext.getUserId());
    }

    @ActionMethod(UserCmdModule.startOrEndBuffTool)
    public StartOrEndBuffToolResPb startOrEndBuffTool(StartOrEndBuffToolReqPb startOrEndBuffToolReqPb, MyFlowContext myFlowContext) throws MsgException{
        return (StartOrEndBuffToolResPb)userService.getExecutor(UserCmdModule.startOrEndBuffToolToolExecutorName).executor(startOrEndBuffToolReqPb,myFlowContext.getUserId());
    }

    @ActionMethod(UserCmdModule.getRedPacket)
    public GetRedPacketResPb getRedPacket(GetRedPacketReqPb getRedPacketReqPb, MyFlowContext myFlowContext) throws MsgException{
        return (GetRedPacketResPb)userService.getExecutor(UserCmdModule.getRedPacketExecutorName).executor(getRedPacketReqPb,myFlowContext.getUserId());
    }

    @ActionMethod(UserCmdModule.challengeMagnateSuccess)
    public ChallengeMagnateSuccessResPb challengeMagnateSuccess(ChallengeMagnateSuccessReqPb challengeMagnateSuccessReqPb, MyFlowContext myFlowContext) throws MsgException{
        return (ChallengeMagnateSuccessResPb)userService.getExecutor(UserCmdModule.challengeMagnateSuccessExecutorName).executor(challengeMagnateSuccessReqPb,myFlowContext.getUserId());
    }

    @ActionMethod(UserCmdModule.challengeBossSuccess)
    public ChallengeBossSuccessResPb challengeBossSuccess(ChallengeBossSuccessReqPb challengeBossSuccessReqPb, MyFlowContext myFlowContext) throws MsgException{
        return (ChallengeBossSuccessResPb)userService.getExecutor(UserCmdModule.challengeBossSuccessExecutorName).executor(challengeBossSuccessReqPb,myFlowContext.getUserId());
    }

    @ActionMethod(UserCmdModule.unlockVehicleOrEquipment)
    public UnlockVehicleOrEquipmentResPb unlockVehicleOrEquipment(UnlockVehicleOrEquipmentReqPb unlockVehicleOrEquipmentReqPb, MyFlowContext myFlowContext) throws MsgException{
        return (UnlockVehicleOrEquipmentResPb)userService.getExecutor(UserCmdModule.unlockVehicleOrEquipmentExecutorName).executor(unlockVehicleOrEquipmentReqPb,myFlowContext.getUserId());
    }

    @ActionMethod(UserCmdModule.logicHeartbeat)
    public LogicHeartbeatResPb logicHeartbeat(LogicHeartbeatReqPb logicHeartbeatReqPb, MyFlowContext myFlowContext) throws MsgException{
        return (LogicHeartbeatResPb)userService.getExecutor(UserCmdModule.logicHeartbeatExecutorName).executor(logicHeartbeatReqPb,myFlowContext.getUserId());
    }

    @ActionMethod(UserCmdModule.gmCommand)
    public GmCommandResPb gmCommand(GmCommandReqPb gmCommandReqPb, MyFlowContext myFlowContext) throws MsgException{
        return (GmCommandResPb)userService.getExecutor(UserCmdModule.gmCommandExecutorName).executor(gmCommandReqPb,myFlowContext.getUserId());
    }

    @ActionMethod(UserCmdModule.watchedAd)
    public WatchedAdResPb watchedAd(WatchedAdReqPb watchedAdReqPb, MyFlowContext myFlowContext) throws MsgException{
        return (WatchedAdResPb)userService.getExecutor(UserCmdModule.watchedAdExecutorName).executor(watchedAdReqPb,myFlowContext.getUserId());
    }

    @ActionMethod(UserCmdModule.selectStone)
    public SelectStoneResPb selectStone(SelectStoneReqPb selectStoneReqPb, MyFlowContext myFlowContext) throws MsgException{
        return (SelectStoneResPb)userService.getExecutor(UserCmdModule.selectStoneExecutorName).executor(selectStoneReqPb,myFlowContext.getUserId());
    }

    @ActionMethod(UserCmdModule.sellStone)
    public SellStoneResPb sellStone(SellStoneReqPb sellStoneReqPb, MyFlowContext myFlowContext) throws MsgException{
        return (SellStoneResPb)userService.getExecutor(UserCmdModule.sellStoneExecutorName).executor(sellStoneReqPb,myFlowContext.getUserId());
    }

    @ActionMethod(UserCmdModule.addClickCount)
    public AddClickCountResPb addClick(AddClickCountReqPb addClickCountReqPb, MyFlowContext myFlowContext) throws MsgException{
        return (AddClickCountResPb)userService.getExecutor(UserCmdModule.addClickCountExecutorName).executor(addClickCountReqPb,myFlowContext.getUserId());
    }

    @ActionMethod(UserCmdModule.vehicleNewLevelUp)
    public VehicleNewLevelUpResPb vehicleNewLevelUp(VehicleNewLevelUpReqPb vehicleNewLevelUpReqPb, MyFlowContext myFlowContext) throws MsgException{
        return (VehicleNewLevelUpResPb)userService.getExecutor(UserCmdModule.vehicleNewLevelUpExecutorName).executor(vehicleNewLevelUpReqPb,myFlowContext.getUserId());
    }

    @ActionMethod(UserCmdModule.lotteryTicketBuy)
    public LotteryTicketBuyResPb lotteryTicketBuy(LotteryTicketBuyReqPb lotteryTicketBuyReqPb, MyFlowContext myFlowContext) throws MsgException{
        return (LotteryTicketBuyResPb)userService.getExecutor(UserCmdModule.lotteryTicketBuyExecutorName).executor(lotteryTicketBuyReqPb,myFlowContext.getUserId());
    }

    @ActionMethod(UserCmdModule.lotteryTicketBonusGet)
    public LotteryTicketBonusGetResPb lotteryTicketBonusGet(LotteryTicketBonusGetReqPb lotteryTicketBonusGetReqPb, MyFlowContext myFlowContext) throws MsgException{
        return (LotteryTicketBonusGetResPb)userService.getExecutor(UserCmdModule.lotteryTicketBonusGetExecutorName).executor(lotteryTicketBonusGetReqPb,myFlowContext.getUserId());
    }

    @ActionMethod(UserCmdModule.userDataFromCacheToDB)
    public UserDataFromCacheToDBResPb userDataFromCacheToDB(UserDataFromCacheToDBReqPb userDataFromCacheToDBReqPb, MyFlowContext myFlowContext) throws MsgException{
        return (UserDataFromCacheToDBResPb)userService.userDataFromCacheToDBByNotify(userDataFromCacheToDBReqPb,myFlowContext.getUserId());
    }

    @ActionMethod(UserCmdModule.drawRoundReady)
    public DrawRoundReadyResPb drawRoundReady(DrawRoundReadyReqPb drawRoundReadyReqPb, MyFlowContext myFlowContext) throws MsgException{
        return (DrawRoundReadyResPb)userService.getExecutor(UserCmdModule.drawRoundReadyExecutorName).executor(drawRoundReadyReqPb,myFlowContext.getUserId());
    }

    @ActionMethod(UserCmdModule.draw)
    public DrawResPb draw(DrawReqPb drawReqPb, MyFlowContext myFlowContext) throws MsgException{
        return (DrawResPb)userService.getExecutor(UserCmdModule.drawExecutorName).executor(drawReqPb,myFlowContext.getUserId());
    }

    @ActionMethod(UserCmdModule.drawUseBag)
    public DrawUseBagResPb drawUseBag(DrawUseBagReqPb drawUseBagReqPb, MyFlowContext myFlowContext) throws MsgException{
        return (DrawUseBagResPb)userService.getExecutor(UserCmdModule.drawUseBagExecutorName).executor(drawUseBagReqPb,myFlowContext.getUserId());
    }

    @ActionMethod(UserCmdModule.drawSettlement)
    public DrawSettlementResPb drawSettlement(DrawSettlementReqPb drawSettlementReqPb, MyFlowContext myFlowContext) throws MsgException{
        return (DrawSettlementResPb)userService.getExecutor(UserCmdModule.drawSettlementExecutorName).executor(drawSettlementReqPb,myFlowContext.getUserId());
    }

    @ActionMethod(UserCmdModule.drawBuffOpen)
    public DrawBuffOpenResPb drawBuffOpen(DrawBuffOpenReqPb drawBuffOpenReqPb, MyFlowContext myFlowContext) throws MsgException{
        return (DrawBuffOpenResPb)userService.getExecutor(UserCmdModule.drawBuffOpenExecutorName).executor(drawBuffOpenReqPb,myFlowContext.getUserId());
    }
}

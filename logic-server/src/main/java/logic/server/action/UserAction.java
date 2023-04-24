package logic.server.action;

import com.iohao.game.action.skeleton.annotation.ActionController;
import com.iohao.game.action.skeleton.annotation.ActionMethod;
import com.iohao.game.action.skeleton.core.exception.MsgException;
import common.pb.cmd.UserCmdModule;
import common.pb.pb.AttributeLevelUpReqPb;
import common.pb.pb.AttributeLevelUpResPb;
import common.pb.pb.ChangeVehicleReqPb;
import common.pb.pb.ChangeVehicleResPb;
import common.pb.pb.GetRedPacketReqPb;
import common.pb.pb.GetRedPacketResPb;
import common.pb.pb.SettlementReqPb;
import common.pb.pb.SettlementResPb;
import common.pb.pb.StartOrEndBuffToolReqPb;
import common.pb.pb.StartOrEndBuffToolResPb;
import common.pb.pb.UseEquipmentReqPb;
import common.pb.pb.UseEquipmentResPb;
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
}

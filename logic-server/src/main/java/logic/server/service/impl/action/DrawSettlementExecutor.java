package logic.server.service.impl.action;

import common.pb.enums.ErrorCodeEnum;
import common.pb.pb.DrawSettlementReqPb;
import common.pb.pb.DrawSettlementResPb;
import logic.server.dto.CfgDrawDTO;
import logic.server.dto.UserDTO;
import logic.server.dto.UserDrawDTO;
import logic.server.service.IPushPbService;
import logic.server.singleton.CfgManagerSingleton;
import logic.server.singleton.UserManagerSingleton;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class DrawSettlementExecutor implements BaseExecutor<DrawSettlementReqPb, DrawSettlementResPb,Long>{
    @Autowired
    private IPushPbService pushPbService;
    @Override
    public DrawSettlementResPb executor(DrawSettlementReqPb arg, Long userId){
        log.info("DrawSettlementExecutor::executor:userId = {},arg = {},start",userId,arg);
        DrawSettlementResPb drawSettlementResPb = new DrawSettlementResPb();

        UserDTO userDTO = UserManagerSingleton.getInstance().getUserByIdFromCache(userId);
        UserDrawDTO userDrawDTO = UserManagerSingleton.getInstance().getUserDrawFromCache(userId);
        if(userDrawDTO == null){
            log.info("DrawSettlementExecutor::executor:userId = {},userDrawDTO is null",userId);
            drawSettlementResPb.setCode(ErrorCodeEnum.drawCommonError.getCode()).setMessage(ErrorCodeEnum.drawCommonError.getMsg());
            return drawSettlementResPb;
        }
        if(userDrawDTO.getRemainDrawCount() > 0 && (userDrawDTO.getSsqCount() < userDrawDTO.getSsqTargetCount())){
            log.info("DrawSettlementExecutor::executor:userId = {},remainDrawCount = {},ssqCount = {},ssqTargetCount = {}, 此回合尚未结束",
                    userId,userDrawDTO.getRemainDrawCount(),userDrawDTO.getSsqCount(),userDrawDTO.getSsqTargetCount());
            drawSettlementResPb.setCode(ErrorCodeEnum.drawCommonError.getCode()).setMessage(ErrorCodeEnum.drawCommonError.getMsg());
            return drawSettlementResPb;
        }
        CfgDrawDTO cfgDrawDTO = CfgManagerSingleton.getInstance().getCfgDrawByRoundNumberFromCache(userDrawDTO.getRoundNumber());
        if(cfgDrawDTO == null){
            log.info("DrawSettlementExecutor::executor:userId = {},roundNumber = {}, cfgDrawDTO is null",userId,userDrawDTO.getRoundNumber());
            drawSettlementResPb.setCode(ErrorCodeEnum.drawCommonError.getCode()).setMessage(ErrorCodeEnum.drawCommonError.getMsg());
            return drawSettlementResPb;
        }

        boolean isSuccess = userDrawDTO.getSsqCount() >= userDrawDTO.getSsqTargetCount() ? true : false;
        long moneyBefore = userDTO.getMoney();
        float multiple = 1.0f;
        if(arg.isWatchedAd){
            // 看完广告并且成功（看广告失败，客户端不会发送过来）
            if(isSuccess) multiple = cfgDrawDTO.getIncludeExtraReward();
        }else{
            // 未看广告
            if(isSuccess){
                // 成功
                multiple = cfgDrawDTO.getReward();
            }else{
                // 失败
                multiple = cfgDrawDTO.getPunish();
            }
        }
        long moneyAfter = (long)(moneyBefore * multiple);
        userDTO.setMoney(moneyAfter);
        log.info("DrawSettlementExecutor::executor:userId = {},moneyBefore = {}, multiple = {},moneyAfter = {}",userId,moneyBefore,multiple,moneyAfter);
        /** 同步金钱数量（推送）**/
        pushPbService.moneySync(userId);

        log.info("DrawSettlementExecutor::executor:userId = {},drawSettlementResPb = {},end",userId,drawSettlementResPb);
        return drawSettlementResPb;
    }
}

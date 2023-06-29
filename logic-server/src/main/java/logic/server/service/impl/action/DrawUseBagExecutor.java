package logic.server.service.impl.action;

import common.pb.enums.ErrorCodeEnum;
import common.pb.pb.DrawUseBagReqPb;
import common.pb.pb.DrawUseBagResPb;
import logic.server.dto.CfgDrawDTO;
import logic.server.dto.UserDrawDTO;
import logic.server.singleton.CfgManagerSingleton;
import logic.server.singleton.UserManagerSingleton;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Random;

@Slf4j
@Service
public class DrawUseBagExecutor implements BaseExecutor<DrawUseBagReqPb, DrawUseBagResPb,Long>{
    @Override
    public DrawUseBagResPb executor(DrawUseBagReqPb arg, Long userId){
        log.info("DrawUseBagExecutor::executor:userId = {},arg = {},start",userId,arg);
        DrawUseBagResPb drawUseBagResPb = new DrawUseBagResPb();

        UserDrawDTO userDrawDTO = UserManagerSingleton.getInstance().getUserDrawFromCache(userId);
        if(userDrawDTO == null){
            log.info("DrawUseBagExecutor::executor:userId = {},userDrawDTO is null",userId);
            drawUseBagResPb.setCode(ErrorCodeEnum.drawCommonError.getCode()).setMessage(ErrorCodeEnum.drawCommonError.getMsg());
            return drawUseBagResPb;
        }
        if(userDrawDTO.getSsqCount() >= userDrawDTO.getSsqTargetCount()){
            log.info("DrawUseBagExecutor::executor:userId = {},上上签数量已达标",userId);
            drawUseBagResPb.setCode(ErrorCodeEnum.drawCommonError.getCode()).setMessage(ErrorCodeEnum.drawCommonError.getMsg());
            return drawUseBagResPb;
        }
        if(userDrawDTO.getRemainDrawCount() <= 0){
            log.info("DrawUseBagExecutor::executor:userId = {},剩余轮数小于等于0",userId);
            drawUseBagResPb.setCode(ErrorCodeEnum.drawCommonError.getCode()).setMessage(ErrorCodeEnum.drawCommonError.getMsg());
            return drawUseBagResPb;
        }
        CfgDrawDTO cfgDrawDTO = CfgManagerSingleton.getInstance().getCfgDrawByRoundNumberFromCache(userDrawDTO.getRoundNumber());
        if(cfgDrawDTO == null){
            log.info("DrawUseBagExecutor::executor:userId = {},roundNumber = {}, cfgDrawDTO is null",userId,userDrawDTO.getRoundNumber());
            drawUseBagResPb.setCode(ErrorCodeEnum.drawCommonError.getCode()).setMessage(ErrorCodeEnum.drawCommonError.getMsg());
            return drawUseBagResPb;
        }
        int addRemainDrawCountPercent = (int)cfgDrawDTO.getBagProbability();
        Random r = new Random();
        int randomPercent = r.nextInt(100) + 1;
        if(randomPercent >= 1 && randomPercent <= addRemainDrawCountPercent){
            // 轮数+1
            userDrawDTO.setRemainDrawCount(userDrawDTO.getRemainDrawCount() + 1);
            drawUseBagResPb.setResultType(0);
        }else{
            // 上上签-1
            userDrawDTO.setSsqCount( (userDrawDTO.getSsqCount() - 1) <= 0 ? 0 : (userDrawDTO.getSsqCount() - 1) );
            drawUseBagResPb.setResultType(1);
        }
        userDrawDTO.setBagUsable(false);
        drawUseBagResPb.setRemainDrawCount(userDrawDTO.getRemainDrawCount()).setSsqCount(userDrawDTO.getSsqCount()).setBagUsable(userDrawDTO.isBagUsable());

        log.info("DrawUseBagExecutor::executor:userId = {},drawUseBagResPb = {},end",userId,drawUseBagResPb);
        return drawUseBagResPb;
    }
}

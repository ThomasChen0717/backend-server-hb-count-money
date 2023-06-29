package logic.server.service.impl.action;

import common.pb.pb.DrawRoundReadyReqPb;
import common.pb.pb.DrawRoundReadyResPb;
import logic.server.dto.CfgDrawDTO;
import logic.server.dto.UserDTO;
import logic.server.dto.UserDrawDTO;
import logic.server.singleton.CfgManagerSingleton;
import logic.server.singleton.UserManagerSingleton;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;

@Slf4j
@Service
public class DrawRoundReadyExecutor implements BaseExecutor<DrawRoundReadyReqPb, DrawRoundReadyResPb,Long>{
    @Override
    public DrawRoundReadyResPb executor(DrawRoundReadyReqPb arg, Long userId){
        log.info("DrawRoundReadyExecutor::executor:userId = {},arg = {},start",userId,arg);
        DrawRoundReadyResPb drawRoundReadyResPb = new DrawRoundReadyResPb();

        CfgDrawDTO cfgDrawDTO = CfgManagerSingleton.getInstance().getCfgDrawByRoundNumberFromCache(arg.getRoundNumber());
        if(cfgDrawDTO != null){
            drawRoundReadyResPb.setRoundNumber(arg.getRoundNumber()).setRemainDrawCount(cfgDrawDTO.getDrawCount()).setSsqCount(0)
                    .setSsqTargetCount(cfgDrawDTO.getSsqTargetCount()).setBagUsable(false);

            UserManagerSingleton.getInstance().removeUserDrawInCache(userId);
            UserDrawDTO userDrawDTO = new UserDrawDTO();
            userDrawDTO.setUserId(userId).setRoundNumber(arg.getRoundNumber()).setRemainDrawCount(cfgDrawDTO.getDrawCount())
                    .setSsqTargetCount(cfgDrawDTO.getSsqTargetCount()).setCreateTime(new Date());
            UserManagerSingleton.getInstance().addUserDrawToCache(userId,userDrawDTO);
        }
        UserDTO userDTO = UserManagerSingleton.getInstance().getUserByIdFromCache(userId);
        if(userDTO != null){
            Date currTime = new Date();
            if(userDTO.getLatestDrawTime() == null) {
                userDTO.setLatestDrawTime(currTime);
            }else{
                var fmt = new SimpleDateFormat("yyyyMMdd");
                if(!fmt.format(userDTO.getLatestDrawTime()).equals(fmt.format(currTime))){
                    userDTO.setLatestDrawTime(currTime);
                }
            }
        }

        log.info("DrawRoundReadyExecutor::executor:userId = {},drawRoundReadyResPb = {},end",userId,drawRoundReadyResPb);
        return drawRoundReadyResPb;
    }
}

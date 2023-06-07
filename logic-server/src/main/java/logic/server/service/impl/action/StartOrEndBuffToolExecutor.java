package logic.server.service.impl.action;

import common.pb.enums.ErrorCodeEnum;
import common.pb.pb.StartOrEndBuffToolReqPb;
import common.pb.pb.StartOrEndBuffToolResPb;
import logic.server.dto.UserBuffToolDTO;
import logic.server.singleton.UserManagerSingleton;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class StartOrEndBuffToolExecutor implements BaseExecutor<StartOrEndBuffToolReqPb, StartOrEndBuffToolResPb,Long> {
    @Override
    public StartOrEndBuffToolResPb executor(StartOrEndBuffToolReqPb arg, Long userId){
        log.info("StartOrEndBuffToolExecutor::executor:userId = {},arg = {},start",userId,arg);
        StartOrEndBuffToolResPb startOrEndBuffToolResPb = new StartOrEndBuffToolResPb();

        UserBuffToolDTO userBuffToolDTO = UserManagerSingleton.getInstance().getUserBuffToolByIdFromCache(userId,arg.getBuffToolId());
        if(userBuffToolDTO == null){
            startOrEndBuffToolResPb.setCode(ErrorCodeEnum.buffToolNotExist.getCode()).setMessage(ErrorCodeEnum.buffToolNotExist.getMsg());
            log.info("StartOrEndBuffToolExecutor::executor:userId = {},startOrEndBuffToolResPb = {},end",userId,startOrEndBuffToolResPb);
            return startOrEndBuffToolResPb;
        }

        userBuffToolDTO.setInUse(arg.isStart);
        userBuffToolDTO.setEffectLeftTime(arg.getEffectLeftTime());

        log.info("StartOrEndBuffToolExecutor::executor:userId = {},startBuffToolResPb = {},end",userId, startOrEndBuffToolResPb);
        return startOrEndBuffToolResPb;
    }
}

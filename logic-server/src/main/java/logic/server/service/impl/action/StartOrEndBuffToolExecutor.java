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
        log.info("StartOrEndBuffToolExecutor::executor:userId = {},start",userId);
        UserBuffToolDTO userBuffToolDTO = UserManagerSingleton.getInstance().getUserBuffToolByIdFromCache(userId,arg.getBuffToolId());
        ErrorCodeEnum.buffToolNotExist.assertNonNull(userBuffToolDTO);

        userBuffToolDTO.setInUse(arg.isStart);

        StartOrEndBuffToolResPb startOrEndBuffToolResPb = new StartOrEndBuffToolResPb();
        log.info("StartOrEndBuffToolExecutor::executor:userId = {},startBuffToolResPb = {},end",userId, startOrEndBuffToolResPb);
        return startOrEndBuffToolResPb;
    }
}

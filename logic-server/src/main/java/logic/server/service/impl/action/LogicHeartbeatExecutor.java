package logic.server.service.impl.action;

import com.iohao.game.bolt.broker.client.kit.ExternalCommunicationKit;
import common.pb.pb.LogicHeartbeatReqPb;
import common.pb.pb.LogicHeartbeatResPb;
import logic.server.dto.UserDTO;
import logic.server.singleton.UserManagerSingleton;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class LogicHeartbeatExecutor implements BaseExecutor<LogicHeartbeatReqPb, LogicHeartbeatResPb,Long> {
    @Override
    public LogicHeartbeatResPb executor(LogicHeartbeatReqPb arg, Long userId){
        log.info("LogicHeartbeatExecutor::executor:userId = {},arg = {},start",userId,arg);
        LogicHeartbeatResPb logicHeartbeatResPb = new LogicHeartbeatResPb();

        UserDTO userDTO = UserManagerSingleton.getInstance().getUserByIdFromCache(userId);
        if(userDTO == null){
            // 缓存数据不存在，说明是逻辑服重启后用户未重新登录，强制用户下线
            log.info("LogicHeartbeatExecutor::executor:userId = {},强制用户下线", userId);
            // （相当于顶号），强制断开之前的客户端连接，并让本次登录成功。
            ExternalCommunicationKit.forcedOffline(userId);
        }

        log.info("GetRedPacketExecutor::executor:userId = {},logicHeartbeatResPb = {},end",userId,logicHeartbeatResPb);
        return logicHeartbeatResPb;
    }
}

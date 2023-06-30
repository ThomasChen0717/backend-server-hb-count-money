package logic.server.service.impl;

import com.alibaba.fastjson.JSONObject;
import logic.server.dto.LogCollectDTO;
import logic.server.repository.LogCollectRepository;
import logic.server.service.ILogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class LogServiceImpl implements ILogService {
    @Autowired
    private LogCollectRepository logCollectRepository;

    @Override
    public int logCollect(JSONObject jsonLog){
        LogCollectDTO logCollectDTO = new LogCollectDTO();
        logCollectDTO.setUserId(jsonLog.getLongValue("userId"))
                .setClientVersion(jsonLog.getString("clientVersion"))
                .setLogInfo(jsonLog.getString("logInfo"));

        return logCollectRepository.add(logCollectDTO);
    }
}

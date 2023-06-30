package logic.server.service;

import com.alibaba.fastjson.JSONObject;

public interface ILogService {
    void logCollect(JSONObject jsonLog);
}

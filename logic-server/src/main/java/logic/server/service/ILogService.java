package logic.server.service;

import com.alibaba.fastjson.JSONObject;

public interface ILogService {
    int logCollect(JSONObject jsonLog);
}

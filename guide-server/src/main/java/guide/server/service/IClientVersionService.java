package guide.server.service;

import com.alibaba.fastjson.JSONObject;

public interface IClientVersionService {
    String getServerUrl(String clientVersion);
    JSONObject getServerUrlNew(String clientVersion);
}

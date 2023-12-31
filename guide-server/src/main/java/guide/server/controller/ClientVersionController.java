package guide.server.controller;

import com.alibaba.fastjson.JSONObject;
import guide.server.service.IClientVersionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("clientVersion")
public class ClientVersionController {
    @Autowired
    private IClientVersionService clientVersionService;

    @RequestMapping(value = "getServerUrl", method = RequestMethod.POST)
    public String getServerUrl(@RequestBody JSONObject jsonBody) {
        /** 1.2.1（含） 之前的版本 **/
        return clientVersionService.getServerUrl(jsonBody.getString ("version"));
    }

    @RequestMapping(value = "getServerUrlNew", method = RequestMethod.POST)
    public JSONObject getServerUrlNew(@RequestBody JSONObject jsonBody) {
        return clientVersionService.getServerUrlNew(jsonBody.getString ("version"));
    }
}

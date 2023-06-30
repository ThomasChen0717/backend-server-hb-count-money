package logic.server.service.controller;

import com.alibaba.fastjson.JSONObject;
import logic.server.service.ILogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("log")
public class LogController {
    @Autowired
    private ILogService logService;

    @RequestMapping(value = "logCollect", method = RequestMethod.POST)
    public int logCollect(@RequestBody JSONObject jsonLog) {
        return logService.logCollect(jsonLog);
    }
}

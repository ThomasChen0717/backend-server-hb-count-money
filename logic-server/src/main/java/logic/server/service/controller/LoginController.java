package logic.server.service.controller;

import com.alibaba.fastjson.JSONObject;
import common.pb.pb.LoginResPb;
import logic.server.service.ILoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("login")
public class LoginController {
    @Autowired
    private ILoginService loginService;

    @RequestMapping(value = "preLogin", method = RequestMethod.POST)
    public JSONObject preLogin(@RequestBody JSONObject jsonPreLogin) {
        return loginService.preLogin(jsonPreLogin);
    }
}

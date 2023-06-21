package logic.server.service.controller;

import com.alibaba.fastjson.JSONObject;
import common.pb.pb.LoginResPb;
import logic.server.service.ILoginService;
import logic.server.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.function.IntUnaryOperator;

@RestController
@RequestMapping("login")
public class LoginController {
    @Autowired
    private ILoginService loginService;
    @Autowired
    private IUserService userService;

    @RequestMapping(value = "preLogin", method = RequestMethod.POST)
    public JSONObject preLogin(@RequestBody JSONObject jsonPreLogin) {
        return loginService.preLogin(jsonPreLogin);
    }

    @RequestMapping(value = "test", method = RequestMethod.POST)
    public String test(@RequestBody JSONObject jsonTest) {
        try{
            //userService.checkSaveDataFromCacheToDB(jsonTest.getInteger("index"));
            userService.checkDeleteHistoryUserData();
        }catch (Exception e){

        }
        return "success";
    }
}

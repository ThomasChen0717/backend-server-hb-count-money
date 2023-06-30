package admin.server.service.controller;

import admin.server.dto.WebUserDTO;
import admin.server.dto.dtoToken;
import admin.server.dto.dtoUserInfo;
import admin.server.entity.APIResponse;
import admin.server.service.IWebLoginService;
import admin.server.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;


@RestController
@RequestMapping("webLogin")
public class WebLoginController {
    @Autowired
    private IWebLoginService webLoginService;

    @PostMapping("/logout")
    public APIResponse logout(@RequestHeader("X-Token") String token){
        APIResponse res = new APIResponse();
        try {
            // 验证token的合法和有效性
            String tokenValue = JwtUtil.verify(token);
            // 获取token中的用户名
            String username = tokenValue.replaceFirst(JwtUtil.TOKEN_SUCCESS, "");
            // 移除session中的登录标记（或者redis中的登录标记）
            res.setMessage("登出成功");
            res.setData("登出成功");
            res.setCode(1);
        } catch(Exception e){
            res.setMessage("登出失败");
            res.setData("登出失败");
            res.setCode(-1);
        }
        return res;
    }



    @GetMapping("/info")
    public APIResponse info(@RequestParam("token") String token){
        APIResponse res = new APIResponse();
        try {
            // 验证token的合法和有效性
            String tokenValue = JwtUtil.verify(token);
            if (tokenValue != null && tokenValue.startsWith(JwtUtil.TOKEN_SUCCESS)) {
                String username = tokenValue.replaceFirst(JwtUtil.TOKEN_SUCCESS, "");
                dtoUserInfo info = new dtoUserInfo();
                info = this.webLoginService.getInfo(username, info);
                res.setData(info);
                res.setMessage("获取用户信息成功");
                res.setCode(1);
            } else {
                res.setCode(50008);
                res.setMessage("获取用户信息失败");
            }
        } catch(Exception e){
            res.setMessage("获取用户信息失败");
            res.setData("获取用户信息失败");
            res.setCode(-1);
        }

        return res;
    }

    @PostMapping("/login")
    public APIResponse login(@RequestBody WebUserDTO user){
        APIResponse res = new APIResponse();
        try {
            //调用WebLoginService完成username和password的验证
            WebUserDTO dto = this.webLoginService.checkCredentials(user);
            //根据验证结果，组成响应对象返回
            if (dto!=null){
                //创建一个token数据，封装到res对象中
                String token = JwtUtil.sign(user.getUsername(), "-1");
                res.setCode(1);
                res.setMessage("成功");
                res.setData(new dtoToken(token));
            }else {
                res.setCode(-1);
                res.setMessage("用户名和密码不匹配");
                res.setData("Fail");
            }
        } catch (Exception e) {
            res.setCode(-1);
            res.setMessage("用户名和密码不匹配");
            res.setData("Fail");
            e.printStackTrace();
        }
        return res;
    }

    @PostMapping("/register")
    public APIResponse register(@RequestBody WebUserDTO user) throws IOException {
        APIResponse res = new APIResponse();
        try {
            this.webLoginService.registerUser(user);
            res.setCode(1);
            res.setMessage("注册成功");
            res.setData("注册成功");
        } catch(Exception e){
            res.setCode(-1);
            res.setMessage("注册失败");
            res.setData("注册失败");
        }
        return res;
    }
}

package admin.server.service.controller;

import admin.server.dto.WebUserDTO;
import admin.server.dto.dtoToken;
import admin.server.dto.dtoUserInfo;
import admin.server.entity.APIResponse;
import admin.server.service.IWebLoginService;
import admin.server.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("web")
public class WebLoginController {
    @Autowired
    private IWebLoginService webLoginService;

    @PostMapping("/logout")
    public APIResponse logout(@RequestHeader("X-Token") String token){
        APIResponse res = new APIResponse();
        // 验证token的合法和有效性
        String tokenValue = JwtUtil.verify(token);
        // 获取token中的用户名
        String username = tokenValue.replaceFirst(JwtUtil.TOKEN_SUCCESS, "");
        // 移除session中的登录标记（或者redis中的登录标记）
        res.setMessage("登出成功");
        res.setData("logout success");
        res.setCode(1);

        return res;
    }



    @GetMapping("/info")
    public APIResponse info(@RequestParam("token") String token){
        APIResponse res = new APIResponse();

        // 验证token的合法和有效性
        String tokenValue = JwtUtil.verify(token);
        if(tokenValue != null && tokenValue.startsWith(JwtUtil.TOKEN_SUCCESS)) {
            String username = tokenValue.replaceFirst(JwtUtil.TOKEN_SUCCESS, "");
            dtoUserInfo info = new dtoUserInfo();
            info = this.webLoginService.getInfo(username, info);
            res.setData(info);
            res.setMessage("成功");
            res.setCode(1);
        }else {
            res.setCode(50008);
            res.setMessage("失败");
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
}

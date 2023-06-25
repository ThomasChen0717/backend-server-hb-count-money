package logic.server.service.controller;

import logic.server.config.NacosConfiguration;
import logic.server.dto.WebUserDTO;
import logic.server.dto.dtoToken;
import logic.server.dto.dtoUserInfo;
import logic.server.entity.APIResponse;
import logic.server.service.IWebLoginService;
import logic.server.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import logic.server.service.IToolService;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("Tool")
public class ToolController {
    @Autowired
    private IToolService toolService;

    @Autowired
    private NacosConfiguration nacosConfiguration;

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
    /*
    * 使用 "端口/Tool/updateFromExcel进行读取
    * 在Body中以以下格式加入path和excelList
    * {
            "path": "excel文档的父路径",
            "excelList":["excel文档1.xlsx"， "excel文档2.xlsx",...]
        }
    */
    @RequestMapping(value = "/updateFile", method = RequestMethod.POST)
    public APIResponse handleFileUpload(@RequestParam("file") List<MultipartFile> files) {
        APIResponse res = new APIResponse();
        List<String> errorFiles = toolService.updateFromExcel(files);
        if (errorFiles.isEmpty()) {
            res.setCode(1);
            res.setData("更新成功！");
            res.setMessage("更新成功！");
        } else {
            String err = "";
            for (int i = 0; i < errorFiles.size(); i++) {
                err += errorFiles.get(i) + "\n";
            }
            res.setCode(-1);
            res.setData(err);
            res.setMessage(err);
        }
        return res;
    }
}

package admin.server.service.controller;

import admin.server.dto.WebUserDTO;
import admin.server.entity.APIResponse;
import admin.server.service.IWebValidateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("webValidate")
public class WebValidateController {
    @Autowired
    private IWebValidateService webValidateService;

    @PostMapping("/checkUsername")
    public APIResponse checkUsername(@RequestBody String Username){
        APIResponse res = new APIResponse();
        try {
            res.setCode(1);
            res.setMessage("用户名匹配");
            res.setData(this.webValidateService.checkUniqueUsername(Username));
        } catch(Exception e){
            res.setCode(-1);
            res.setMessage("检查用户名失败");
            res.setData("检查用户名失败");
        }
        return res;
    }

    @PostMapping("/checkDisplayName")
    public APIResponse checkDisplayName(@RequestBody String DisplayName){
        APIResponse res = new APIResponse();
        try {
            res.setCode(1);
            res.setMessage("昵称匹配");
            res.setData(this.webValidateService.checkUniqueDisplayName(DisplayName));
        } catch(Exception e){
            res.setCode(-1);
            res.setMessage("检查昵称失败");
            res.setData("检查昵称失败");
        }
        return res;
    }

    @PostMapping("/checkOldPassword")
    public APIResponse checkOldPassword(@RequestBody WebUserDTO dto){
        APIResponse res = new APIResponse();
        try {
            res.setCode(1);
            res.setMessage("密码匹配");
            res.setData(this.webValidateService.checkOldPassword(dto.getPassword(), dto.getId()));
        } catch(Exception e){
            res.setCode(-1);
            res.setMessage("检查密码失败");
            res.setData("检查密码失败");
        }
        return res;
    }
}

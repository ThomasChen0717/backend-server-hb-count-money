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
        res.setCode(1);
        res.setMessage("成功");
        res.setData(this.webValidateService.checkUniqueUsername(Username));
        return res;
    }

    @PostMapping("/checkDisplayName")
    public APIResponse checkDisplayName(@RequestBody String DisplayName){
        APIResponse res = new APIResponse();
        res.setCode(1);
        res.setMessage("成功");
        res.setData(this.webValidateService.checkUniqueDisplayName(DisplayName));
        return res;
    }

    @PostMapping("/checkOldPassword")
    public APIResponse checkOldPassword(@RequestBody WebUserDTO dto){
        APIResponse res = new APIResponse();
        res.setCode(1);
        res.setMessage("密码匹配");
        res.setData(this.webValidateService.checkOldPassword(dto.getPassword(), dto.getId()));
        return res;
    }
}

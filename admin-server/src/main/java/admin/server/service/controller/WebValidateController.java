package admin.server.service.controller;

import admin.server.dto.WebUserDTO;
import admin.server.entity.APIResponse;
import admin.server.service.IWebValidateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 后端接口RestController: 信息检查操作
 *
 * @author Thomas
 * @date 2023-07-06
 */
@Slf4j
@RestController
@RequestMapping("webValidate")
public class WebValidateController {
    @Autowired
    private IWebValidateService webValidateService;

    /**
     * 检查用户名是否已存在
     *
     * @Param String Username 需要检查的用户名
     * @Retyrn APIResponse 传回前端的回复
     *
     */
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
            log.error("WebValidateServiceImpl::checkUsername Failure:检查用户名失败");
        }
        return res;
    }

    /**
     * 检查用户名是否已存在
     *
     * @Param String DisplayName 需要检查的用户名
     * @Retyrn APIResponse 传回前端的回复
     *
     */
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
            log.error("WebValidateServiceImpl::checkDisplayName Failure:检查昵称失败");
        }
        return res;
    }

    /**
     * 检查老密码是否匹配
     *
     * @Param WebUserDTO dto 需要检查的密码
     * @Retyrn APIResponse 传回前端的回复
     *
     */
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
            log.error("WebValidateServiceImpl::checkOldPassword Failure:检查老密码失败");
        }
        return res;
    }
}

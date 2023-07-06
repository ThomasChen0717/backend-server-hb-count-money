package admin.server.service.controller;
import admin.server.dto.WebUserDTO;
import admin.server.entity.APIResponse;
import admin.server.service.IWebInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * 后端接口RestController: 用户信息更新操作
 *
 * @author Thomas
 * @date 2023-07-06
 */
@Slf4j
@RestController
@RequestMapping("webInfo")
public class WebInfoController {
    @Autowired
    private IWebInfoService webInfoService;

    /**
     * 将前端传来的新密码替换数据库中的老密码
     *
     * @Param WebUserDTO dto
     * @Retyrn APIResponse 传回前端的回复
     *
     */
    @PostMapping("/changePassword")
    public APIResponse changePassword(@RequestBody WebUserDTO dto) {
        APIResponse res = new APIResponse();
        try {
            this.webInfoService.changePassword(dto.getPassword(), dto.getId());
            res.setCode(1);
            res.setMessage("密码更新成功");
            res.setData("密码更新成功");
        } catch (Exception e) {
            res.setCode(-1);
            res.setMessage("密码更新失败");
            res.setData("密码更新失败");
            log.error("WebInfoServiceImpl::changePassword Failure:密码更新失败");
        }
        return res;
    }

    /**
     * 将前端传来的新昵称替换数据库中的老昵称
     *
     * @Param WebUserDTO dto
     * @Retyrn APIResponse 传回前端的回复
     *
     */
    @PostMapping("/changeName")
    public APIResponse changeName(@RequestBody WebUserDTO dto) {
        APIResponse res = new APIResponse();
        try {
            this.webInfoService.changeName(dto.getName(), dto.getId());
            res.setCode(1);
            res.setMessage("昵称更新成功");
            res.setData("昵称更新成功");
        } catch (Exception e) {
            res.setCode(-1);
            res.setMessage("昵称更新失败");
            res.setData("昵称更新失败");
            log.error("WebInfoServiceImpl::changeName Failure:昵称更新失败");
        }
        return res;
    }

    /**
     * 将前端传来的新头像替换数据库中的老头像
     *
     * @Param WebUserDTO dto
     * @Retyrn APIResponse 传回前端的回复
     *
     */
    @PostMapping("/changeAvatar")
    public APIResponse changeAvatar(@RequestParam("image") MultipartFile file, @RequestParam("id") long id) {
        APIResponse res = new APIResponse();
        try {
            this.webInfoService.changeAvatar(file, id);
            res.setCode(1);
            res.setMessage("头像更新成功");
            res.setData("头像更新成功");
        } catch (Exception e) {
            res.setCode(-1);
            res.setMessage("头像更新失败");
            res.setData("头像更新失败");
            log.error("WebInfoServiceImpl::changePassword Failure:头像更新失败");
        }
        return res;
    }
}

package admin.server.service.controller;
import admin.server.dto.WebUserDTO;
import admin.server.entity.APIResponse;
import admin.server.service.IWebInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("webInfo")
public class WebInfoController {
    @Autowired
    private IWebInfoService webInfoService;

    @PostMapping("/changePassword")
    public APIResponse changePassword(@RequestBody WebUserDTO dto){
        APIResponse res = new APIResponse();
        boolean success = this.webInfoService.changePassword(dto.getPassword(), dto.getId());
        if(success) {
            res.setCode(1);
            res.setMessage("密码更新成功");
            res.setData("密码更新成功");
        }
        else{
            res.setCode(-1);
            res.setMessage("密码更新失败");
            res.setData("密码更新失败");
        }
        return res;
    }

    @PostMapping("/changeName")
    public APIResponse changeName(@RequestBody WebUserDTO dto){
        APIResponse res = new APIResponse();
        boolean success = this.webInfoService.changeName(dto.getName(), dto.getId());
        if(success) {
            res.setCode(1);
            res.setMessage("昵称更新成功");
            res.setData("昵称更新成功");
        }
        else{
            res.setCode(-1);
            res.setMessage("昵称更新失败");
            res.setData("昵称更新失败");
        }
        return res;
    }

    @PostMapping("/changeAvatar")
    public APIResponse changeAvatar(@RequestParam("image") MultipartFile file, @RequestParam("id") long id){
        APIResponse res = new APIResponse();
        boolean success = this.webInfoService.changeAvatar(file, id);
        if(success) {
            res.setCode(1);
            res.setMessage("头像更新成功");
            res.setData("头像更新成功");
        }
        else{
            res.setCode(-1);
            res.setMessage("头像更新失败");
            res.setData("头像更新失败");
        }
        return res;
    }
}

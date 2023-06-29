package admin.server.service.controller;

import admin.server.dto.WebRouterDTO;
import admin.server.entity.APIResponse;
import admin.server.entity.Role;
import admin.server.service.WebRouterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("router")
public class WebRouterController {

    @Autowired
    WebRouterService webRouterService;
    @PostMapping("/getRoles")
    public APIResponse getRoles(@RequestBody WebRouterDTO dto){
        APIResponse res = new APIResponse();
        try{
            List<String> roleList = this.webRouterService.getRoles(dto);
            res.setCode(1);
            res.setData(roleList);
            res.setMessage("获取路由成功!");
        }catch(Exception e){
            res.setCode(-1);
            res.setData(null);
            res.setMessage("获取路由失败!");
        }
        return res;
    }

    @PostMapping("/deleteRole")
    public APIResponse deleteRole(@RequestBody String role){
        APIResponse res = new APIResponse();
        try{
            this.webRouterService.deleteRole(role);
            res.setCode(1);
            res.setData("删除成功!");
            res.setMessage("删除成功!");
        }catch(Exception e){
            res.setCode(-1);
            res.setData("删除失败!");
            res.setMessage("删除失败!");
        }
        return res;
    }

    @PostMapping("/addRole")
    public APIResponse addRole(@RequestBody Role role){
        APIResponse res = new APIResponse();
        try{
            boolean success = this.webRouterService.addRole(role);
            res.setCode(1);
            res.setData("添加成功!");
            res.setMessage("添加成功!");
            if(success != true){
                res.setCode(-1);
                res.setData("权限名已存在!");
                res.setMessage("权限名已存在!");
            }
        }catch(Exception e){
            res.setCode(-1);
            res.setData("添加失败!");
            res.setMessage("添加失败!");
        }
        return res;
    }

    @PostMapping("/updateRole")
    public APIResponse updateRole(@RequestBody Role role){
        APIResponse res = new APIResponse();
        try{
            this.webRouterService.updateRole(role);
            res.setCode(1);
            res.setData("删除成功!");
            res.setMessage("删除成功!");
        }catch(Exception e){
            res.setCode(-1);
            res.setData("删除失败!");
            res.setMessage("删除失败!");
        }
        return res;
    }
}

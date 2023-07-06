package admin.server.service.controller;

import admin.server.dto.WebRouterDTO;
import admin.server.entity.APIResponse;
import admin.server.dto.RoleDTO;
import admin.server.service.IWebRouterService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 后端接口RestController: 路由权限操作
 *
 * @author Thomas
 * @date 2023-07-06
 */
@Slf4j
@RestController
@RequestMapping("router")
public class WebRouterController {

    @Autowired
    IWebRouterService webRouterService;

    /**
     * 获取路由的权限列表
     *
     * @Param WebRouterDTO dto 路由
     * @Retyrn APIResponse 传回前端的回复
     *
     */
    @PostMapping("/getRoles")
    public APIResponse getRoles(@RequestBody WebRouterDTO dto){
        APIResponse res = new APIResponse();
        try{
            List<String> roleList = this.webRouterService.getRoles(dto);
            res.setCode(1);
            res.setData(roleList);
            res.setMessage("获取权限成功!");
        }catch(Exception e){
            res.setCode(-1);
            res.setData(null);
            res.setMessage("获取权限失败!");
            log.error("WebRouterServiceImpl::getRoles Failure:获取权限失败");
        }
        return res;
    }

    /**
     * 删除权限
     *
     * @Param String role 需要删除的权限
     * @Retyrn APIResponse 传回前端的回复
     *
     */
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
            log.error("WebRouterController::deleteRole Failure:权限删除失败");
        }
        return res;
    }

    /**
     * 添加权限
     *
     * @Param RoleDTO roleDTO 需要添加的权限
     * @Retyrn APIResponse 传回前端的回复
     *
     */
    @PostMapping("/addRole")
    public APIResponse addRole(@RequestBody RoleDTO roleDTO){
        APIResponse res = new APIResponse();
        try{
            boolean success = this.webRouterService.addRole(roleDTO);
            res.setCode(1);
            res.setData("添加成功!");
            res.setMessage("添加成功!");
            if(success != true){
                res.setCode(-1);
                res.setData("权限名已存在!");
                res.setMessage("权限名已存在!");
                log.error("WebRouterController::addRole Failure(Role Already Exists):权限添加失败(权限已存在)");
            }
        }catch(Exception e){
            res.setCode(-1);
            res.setData("添加失败!");
            res.setMessage("添加失败!");
            log.error("WebRouterServiceImpl::addRole Failure:权限添加失败");
        }
        return res;
    }

    /**
     * 更新权限
     *
     * @Param RoleDTO roleDTO 需要更新的权限
     * @Retyrn APIResponse 传回前端的回复
     *
     */
    @PostMapping("/updateRole")
    public APIResponse updateRole(@RequestBody RoleDTO roleDTO){
        APIResponse res = new APIResponse();
        try{
            this.webRouterService.updateRole(roleDTO);
            res.setCode(1);
            res.setData("更新成功!");
            res.setMessage("更新成功!");
        }catch(Exception e){
            res.setCode(-1);
            res.setData("更新失败!");
            res.setMessage("更新失败!");
            log.error("WebRouterServiceImpl::updateRole Failure:权限更新失败");

        }
        return res;
    }
}

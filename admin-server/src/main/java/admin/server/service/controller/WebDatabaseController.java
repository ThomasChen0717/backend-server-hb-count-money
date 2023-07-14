package admin.server.service.controller;

import admin.server.config.NacosConfiguration;
import admin.server.dto.ClientVersionDTO;
import admin.server.dto.UserCountFilterDTO;
import admin.server.entity.APIResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import admin.server.service.IWebDatabaseService;

import java.time.LocalDateTime;
import java.util.List;


/**
 * 后端接口RestController: 数据库操作
 *
 * @author Thomas
 * @date 2023-07-06
 */
@Slf4j
@RestController
@RequestMapping("database")
public class WebDatabaseController {
    @Autowired
    private IWebDatabaseService webDatabaseService;

    @Autowired
    private NacosConfiguration nacosConfiguration;

    /**
     * 将前端指定时间范围内的t_user_count 表传输回前端（以表的形式显示）
     *
     * @Param UserCountFilterDTO dto 前端所需的时间段
     * @Return APIResponse 传回前端的回复
     *
     */
    @PostMapping("/getUserCount")
    public APIResponse getUserCount(@RequestBody UserCountFilterDTO dto){
        APIResponse res = new APIResponse();
        try {
            res.setMessage("获取表格成功");
            res.setCode(1);
            res.setData(webDatabaseService.getUserCount(dto.getDate(), dto.getHour()));
        } catch(Exception e){
            res.setMessage("获取表格失败");
            res.setCode(-1);
            res.setData("获取表格失败");
            log.error("WebDatabaseServiceImpl::getUserCount Failure:获取表格失败");
        }
        return res;
    }
    /**
     * 将前端指定时间范围内的t_user_count 表传输回前端（以图的形式显示）
     *
     * @Param UserCountFilterDTO dto 前端所需的时间段
     * @Return APIResponse 传回前端的回复
     *
     */
    @PostMapping("/getUserCountGraph")
    public APIResponse getUserCountGraph(@RequestBody UserCountFilterDTO dto){
        APIResponse res = new APIResponse();
        try {
            res.setMessage("获取表格成功");
            res.setCode(1);
            res.setData(webDatabaseService.getUserCountGraph(dto.getDate(), dto.getHour(), dto.getLogicServerId()));
        } catch(Exception e){
            res.setMessage("获取表格失败");
            res.setCode(-1);
            res.setData("获取表格失败");
            log.error("WebDatabaseServiceImpl::getUserCountGraph Failure:获取图失败");
        }
        return res;
    }

    /**
     * 获取t_client_version的数据并传输给前端
     *
     *
     * @Return APIResponse 传回前端的回复
     *
     */
    @GetMapping("/getClientVersion")
    public APIResponse getClientVersion(){
        APIResponse res = new APIResponse();
        try {
            res.setMessage("获取版本信息成功");
            res.setCode(1);
            res.setData(webDatabaseService.getClientVersion());
        } catch(Exception e){
            res.setMessage("获取版本信息失败");
            res.setCode(-1);
            res.setData("获取版本信息失败");
            log.error("WebDatabaseServiceImpl::getClientVersion Failure:获取版本信息失败");
        }
        return res;
    }

    /**
     * 往t_client_version表里添加新字段
     *
     * @Param ClientVersionDTO clientVersionDTO 需要添加的字段
     * @Return APIResponse 传回前端的回复
     *
     */
    @PostMapping("/addNewClientVersion")
    public APIResponse addNewClientVersion(@RequestBody ClientVersionDTO clientVersionDTO){
        APIResponse res = new APIResponse();
        try{
            webDatabaseService.addNewClientVersion(clientVersionDTO);
            res.setMessage("添加新版本成功");
            res.setCode(1);
            res.setData("添加新版本成功");
        } catch(Exception e){
            res.setMessage("添加新版本失败");
            res.setCode(-1);
            res.setData("添加新版本失败");
            log.error("WebDatabaseServiceImpl::addNewClientVersion Failure:添加新版本失败");
        }
        return res;
    }

    /**
     * 往t_client_version表里更新字段
     *
     * @Param ClientVersionDTO clientVersionDTO 需要更新的字段
     * @Return APIResponse 传回前端的回复
     *
     */
    @PostMapping("/updateClientVersion")
    public APIResponse updateClientVersion(@RequestBody ClientVersionDTO clientVersionDTO){
        APIResponse res = new APIResponse();
        try{
            webDatabaseService.updateClientVersion(clientVersionDTO);
            res.setMessage("修改版本成功");
            res.setCode(1);
            res.setData("修改版本成功");
        } catch(Exception e){
            res.setMessage("修改版本失败");
            res.setCode(-1);
            res.setData("修改版本失败");
            log.error("WebDatabaseServiceImpl::updateClientVersion Failure:修改版本失败");
        }
        return res;
    }

    /**
     * 往t_client_version表里删除一个字段
     *
     * @Param ClientVersionDTO clientVersionDTO 需要删除的字段
     * @Return APIResponse 传回前端的回复
     *
     */
    @PostMapping("/deleteClientVersion")
    public APIResponse deleteClientVersion(@RequestBody long id){
        APIResponse res = new APIResponse();
        try{
            webDatabaseService.deleteClientVersion(id);
            res.setMessage("删除版本成功");
            res.setCode(1);
            res.setData("删除版本成功");
        } catch(Exception e){
            res.setMessage("删除版本失败");
            res.setCode(-1);
            res.setData("删除版本失败");
            log.error("WebDatabaseServiceImpl::deleteClientVersion Failure:删除版本失败");
        }
        return res;
    }

    /**
     * 获取一个时间段内的统计信息
     *
     * @Param List<LocalDateTime> dateRange 时间段
     * @Return APIResponse 传回前端的回复
     *
     */
    @PostMapping("getUserActivity")
    public APIResponse getUserActivity(@RequestBody List<LocalDateTime> dateRange){
        APIResponse res = new APIResponse();
        try{
            res.setMessage("获取用户活跃成功");
            res.setCode(1);
            res.setData(webDatabaseService.getUserActivity(dateRange));
        } catch(Exception e){
            res.setMessage("获取用户活跃失败");
            res.setCode(-1);
            res.setData("获取用户活跃失败");
            log.error("WebDatabaseServiceImpl::getUserActivity Failure:获取用户活跃失败");
        }
        return res;
    }

    /**
     * 获取一个时间段内的分成每天每小时的统计信息
     *
     * @Param List<LocalDateTime> dateRange 时间段
     * @Return APIResponse 传回前端的回复
     *
     *
     */
    @PostMapping("getUserActivityTable")
    public APIResponse getUserActivityTable(@RequestBody List<LocalDateTime> dateRange){
        APIResponse res = new APIResponse();
        try{
            res.setMessage("获取用户活跃表成功");
            res.setCode(1);
            res.setData(webDatabaseService.getUserActivityTable(dateRange));
        } catch(Exception e){
            res.setMessage("获取用户活跃表失败");
            res.setCode(-1);
            res.setData("获取用户活跃表失败");
            log.error("WebDatabaseServiceImpl::getUserActivityTable Failure:获取用户活跃表失败");
        }
        return res;
    }

}

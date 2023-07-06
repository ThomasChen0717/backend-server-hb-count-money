package admin.server.service.controller;

import admin.server.config.NacosConfiguration;
import admin.server.dto.UserCountFilterDTO;
import admin.server.entity.APIResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import admin.server.service.IWebDatabaseService;
import org.springframework.web.multipart.MultipartFile;

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
     * 将前端上传的Excel表格存入数据库
     *
     * @Param List<MultipartFile> 前端上传的单个或多个Excel 表格
     * @Retyrn APIResponse 传回前端的回复
     *
     */
    @PostMapping("/updateFile")
    public APIResponse handleFileUpload(@RequestParam("file") List<MultipartFile> files) {
        int allowed = nacosConfiguration.getSpringProfilesActive().compareTo("dev") == 0 || nacosConfiguration.getSpringProfilesActive().compareTo("test") == 0 ? 1 : 0;
        APIResponse res = new APIResponse();
        if(allowed == 1) {
            List<String> errorFiles = webDatabaseService.updateFromExcel(files);
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
        res.setCode(-1);
        res.setData("权限受限！");
        res.setMessage("权限受限！");
        return res;
    }

    /**
     * 将前端指定时间范围内的t_user_count 表传输回前端（以表的形式显示）
     *
     * @Param UserCountFilterDTO dto 前端所需的时间段
     * @Retyrn APIResponse 传回前端的回复
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
     * @Retyrn APIResponse 传回前端的回复
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
            log.error("WebDatabaseServiceImpl::getUserCount Failure:获取图失败");
        }
        return res;
    }
}

package admin.server.service.controller;

import admin.server.config.NacosConfiguration;
import admin.server.dto.UserCountDTO;
import admin.server.dto.UserCountFilterDTO;
import admin.server.entity.APIResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import admin.server.service.IWebDatabaseService;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("database")
public class WebDatabaseController {
    @Autowired
    private IWebDatabaseService webDatabaseService;

    @Autowired
    private NacosConfiguration nacosConfiguration;

    /*
    * 使用 "端口/database/updateFromExcel进行读取
    * 在Body中以以下格式加入path和excelList
    * {
            "path": "excel文档的父路径",
            "excelList":["excel文档1.xlsx"， "excel文档2.xlsx",...]
        }
    */
    @RequestMapping(value = "/updateFile", method = RequestMethod.POST)
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
        }
        return res;
    }
}

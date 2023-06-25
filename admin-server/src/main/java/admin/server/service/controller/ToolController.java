package admin.server.service.controller;

import admin.server.config.NacosConfiguration;
import admin.server.entity.APIResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import admin.server.service.IToolService;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("tool")
public class ToolController {
    @Autowired
    private IToolService toolService;

    @Autowired
    private NacosConfiguration nacosConfiguration;

    /*
    * 使用 "端口/Tool/updateFromExcel进行读取
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
            List<String> errorFiles = toolService.updateFromExcel(files);
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
}

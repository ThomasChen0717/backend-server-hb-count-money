package admin.server.service.controller;

import admin.server.config.NacosConfiguration;
import admin.server.entity.APIResponse;
import admin.server.service.IWebDatabaseService;
import admin.server.service.IWebExcelUploadService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 后端接口RestController: 上传Excel至数据库
 *
 * @author Thomas
 * @date 2023-07-07
 */
@Slf4j
@RestController
@RequestMapping("excel")
public class WebExcelUploadController {

    @Autowired
    private IWebExcelUploadService webExcelUploadService;

    @Autowired
    private NacosConfiguration nacosConfiguration;

    /**
     * 将前端上传的Excel表格存入数据库
     *
     * @Param List<MultipartFile> 前端上传的单个或多个Excel 表格
     * @Return APIResponse 传回前端的回复
     *
     */
    @PostMapping("/updateFile")
    public APIResponse handleFileUpload(@RequestParam("file") List<MultipartFile> files) {
        int allowed = nacosConfiguration.getSpringProfilesActive().compareTo("dev") == 0 || nacosConfiguration.getSpringProfilesActive().compareTo("test") == 0 ? 1 : 0;
        APIResponse res = new APIResponse();
        if(allowed == 1) {
            List<String> errorFiles = webExcelUploadService.updateFromExcel(files);
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

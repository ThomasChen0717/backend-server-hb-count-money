package logic.server.service.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import logic.server.config.NacosConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import logic.server.service.IToolService;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("Tool")
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
    @RequestMapping(value = "updateFromExcel", method = RequestMethod.GET)
    public String updateFromExcel(@RequestBody JSONObject jsonBody) {
        int allowed = nacosConfiguration.getSpringProfilesActive().compareTo("dev") == 0 || nacosConfiguration.getSpringProfilesActive().compareTo("test") == 0 ? 1 : 0;
        if (allowed == 1) {
            JSONArray arr = jsonBody.getJSONArray("excelList");
            List<String> fileNames = new ArrayList<String>();
            for (int i = 0; i < arr.size(); i++) {
                fileNames.add(arr.getString(i));
            }
            List<String> errorFiles = toolService.updateFromExcel(jsonBody.getString("path"), fileNames);
            if (errorFiles.isEmpty()) {
                return "更新成功！";
            } else {
                String res = "";
                for (int i = 0; i < errorFiles.size(); i++) {
                    res += errorFiles.get(i) + "\n";
                }
                return res;
            }
        }
        return "权限受限";
    }
}

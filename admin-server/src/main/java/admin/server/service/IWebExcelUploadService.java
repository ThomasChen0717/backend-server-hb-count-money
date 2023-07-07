package admin.server.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IWebExcelUploadService {
    List<String> updateFromExcel(List<MultipartFile> files);
}

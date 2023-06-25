package admin.server.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IToolService {
    List<String> updateFromExcel(List<MultipartFile> files);
}

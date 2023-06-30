package admin.server.service;

import admin.server.dto.UserCountDTO;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

public interface IWebDatabaseService {
    List<String> updateFromExcel(List<MultipartFile> files);

    List<UserCountDTO> getUserCount(LocalDateTime date, String hour);
}

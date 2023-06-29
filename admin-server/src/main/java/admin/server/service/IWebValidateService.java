package admin.server.service;

import admin.server.dto.WebUserDTO;
import admin.server.dto.dtoUserInfo;
import org.springframework.web.multipart.MultipartFile;

public interface IWebValidateService {
    boolean checkUniqueUsername(String Username);

    boolean checkUniqueDisplayName(String DisplayName);

    boolean checkOldPassword(String password, long id);
}


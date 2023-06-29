package admin.server.service;

import admin.server.dto.WebUserDTO;
import admin.server.dto.dtoUserInfo;
import org.springframework.web.multipart.MultipartFile;

public interface IWebLoginService {

        WebUserDTO checkCredentials(WebUserDTO user);
        dtoUserInfo getInfo(String Username, dtoUserInfo dto);

        boolean registerUser(WebUserDTO user);
}


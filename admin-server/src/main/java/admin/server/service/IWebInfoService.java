package admin.server.service;

import admin.server.dto.WebUserDTO;
import admin.server.dto.dtoUserInfo;
import org.springframework.web.multipart.MultipartFile;

public interface IWebInfoService {
    boolean changePassword(String password, long id);

    boolean changeName(String name, long id);

    boolean changeAvatar(MultipartFile file, long id);
}


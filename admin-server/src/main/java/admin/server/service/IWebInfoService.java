package admin.server.service;

import admin.server.dto.WebUserDTO;
import admin.server.dto.dtoUserInfo;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface IWebInfoService {
    void changePassword(String password, long id);

    void changeName(String name, long id);

    void changeAvatar(MultipartFile file, long id) throws IOException;
}


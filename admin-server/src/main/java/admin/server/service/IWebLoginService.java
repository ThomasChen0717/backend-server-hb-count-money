package admin.server.service;

import admin.server.dto.WebUserDTO;
import admin.server.dto.dtoUserInfo;

    public interface IWebLoginService {
        WebUserDTO checkCredentials(WebUserDTO user);

        dtoUserInfo getInfo(String Username, dtoUserInfo dto);
    }


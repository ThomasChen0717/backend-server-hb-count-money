package logic.server.service;

import logic.server.dto.WebUserDTO;
import logic.server.dto.dtoUserInfo;

    public interface IWebLoginService {
        WebUserDTO checkCredentials(WebUserDTO user);

        dtoUserInfo getInfo(String Username, dtoUserInfo dto);
    }


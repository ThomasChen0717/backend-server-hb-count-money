package admin.server.service.impl;
import lombok.extern.slf4j.Slf4j;
import admin.server.dto.WebUserDTO;
import admin.server.dto.dtoUserInfo;
import admin.server.repository.WebUserRepository;
import admin.server.service.IWebLoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.io.IOException;
import java.util.Map;

import static admin.server.util.Base64Utils.convertToBase64;

@Slf4j
@Service
public class WebLoginServiceImpl implements IWebLoginService {
    @Autowired
    private WebUserRepository webUserRepository;

    @Override
    public WebUserDTO checkCredentials(WebUserDTO user){
        Map<String, WebUserDTO> webUserDTOMap = webUserRepository.getMap();
        String Username = user.getUsername();
        String Password = user.getPassword();
        if(webUserDTOMap.containsKey(Username) && webUserDTOMap.get(Username).getPassword().equals(Password)) {
            return user;
        }
        else {
            return null;
        }
    }

    @Override
    public dtoUserInfo getInfo(String username, dtoUserInfo dto){
        Map<String, WebUserDTO> webUserDTOMap = webUserRepository.getMap();
        dto.setAvatar(webUserDTOMap.get(username).getAvatar());
        dto.setName(webUserDTOMap.get(username).getName());
        dto.setRole(webUserDTOMap.get(username).getRole());
        dto.setId(webUserDTOMap.get(username).getId());
        return dto;
    }

    @Override
    public void registerUser(WebUserDTO user) throws IOException {
        String defaultAvatar = "https://th.bing.com/th/id/OIP.0siT9Vkwx8tb_kFTi-KV1wHaHa?pid=ImgDet&rs=1";
        defaultAvatar = "data:image/png;base64," + convertToBase64(defaultAvatar);
        String defaultRole = "editor";
        user.setRole(defaultRole).setAvatar(defaultAvatar);
        webUserRepository.add(user);
    }


}

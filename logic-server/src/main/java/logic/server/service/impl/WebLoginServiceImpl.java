package logic.server.service.impl;
import lombok.extern.slf4j.Slf4j;
import logic.server.dto.WebUserDTO;
import logic.server.dto.dtoUserInfo;
import logic.server.repository.WebUserRepository;
import logic.server.service.IWebLoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
@Slf4j
@Service
public class WebLoginServiceImpl implements IWebLoginService {
    @Autowired
    WebUserRepository webUserRepository;
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
        return dto;
    }

}

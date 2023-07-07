package admin.server.service.impl;
import admin.server.service.IWebInfoService;
import lombok.extern.slf4j.Slf4j;
import admin.server.dto.WebUserDTO;
import admin.server.repository.WebUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

import static admin.server.util.Base64Utils.convertToBase64;
import static admin.server.util.rolesUtil.convertRoles;

@Slf4j
@Service
public class WebInfoServiceImpl implements IWebInfoService {
    @Autowired
    private WebUserRepository webUserRepository;

    @Override
    public void changePassword(String password, long id) {
        Map<Long, WebUserDTO> webUserDTOMap = webUserRepository.getMapById();
        WebUserDTO webUserDTO = webUserDTOMap.get(id).setPassword(password);
        webUserRepository.update(webUserDTO);
    }

    @Override
    public void changeName(String name, long id) {
        Map<Long, WebUserDTO> webUserDTOMap = webUserRepository.getMapById();
        WebUserDTO webUserDTO = webUserDTOMap.get(id).setName(name);
        webUserRepository.update(webUserDTO);
    }

    @Override
    public void changeAvatar(MultipartFile file, long id) throws IOException {
        Map<Long, WebUserDTO> webUserDTOMap = webUserRepository.getMapById();
        String avatarUrl = convertToBase64(file);
        if(file.getOriginalFilename().contains(".jpg")) {
            avatarUrl = "data:image/jpeg;base64," + avatarUrl;
        }
        else if(file.getOriginalFilename().contains(".png")) {
            avatarUrl = "data:image/png;base64," + avatarUrl;
        }
        WebUserDTO webUserDTO = webUserDTOMap.get(id).setAvatar(avatarUrl);
        int bool = webUserRepository.update(webUserDTO);
    }

}

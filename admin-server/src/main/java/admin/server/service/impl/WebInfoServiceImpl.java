package admin.server.service.impl;
import admin.server.service.IWebInfoService;
import lombok.extern.slf4j.Slf4j;
import admin.server.dto.WebUserDTO;
import admin.server.repository.WebUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

import static admin.server.util.Base64Utils.convertToBase64;
import static admin.server.util.rolesUtil.convertRoles;

@Slf4j
@Service
public class WebInfoServiceImpl implements IWebInfoService {
    @Autowired
    WebUserRepository webUserRepository;

    @Override
    public boolean changePassword(String password, long id) {
        try {
            Map<Long, WebUserDTO> webUserDTOMap = webUserRepository.getMapById();
            WebUserDTO dto = webUserDTOMap.get(id).setPassword(password);
            int bool = webUserRepository.update(dto);
            if(bool != 1){
                return false;
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    @Override
    public boolean changeName(String name, long id) {
        try {
            Map<Long, WebUserDTO> webUserDTOMap = webUserRepository.getMapById();
            WebUserDTO dto = webUserDTOMap.get(id).setName(name);
            int bool = webUserRepository.update(dto);
            if(bool != 1){
                return false;
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    @Override
    public boolean changeAvatar(MultipartFile file, long id) {
        try {
            Map<Long, WebUserDTO> webUserDTOMap = webUserRepository.getMapById();
            String avatarUrl = convertToBase64(file);
            if(file.getOriginalFilename().contains(".jpg")) {
                avatarUrl = "data:image/jpeg;base64," + avatarUrl;
            }
            else if(file.getOriginalFilename().contains(".png")) {
                avatarUrl = "data:image/png;base64," + avatarUrl;
            }
            WebUserDTO dto = webUserDTOMap.get(id).setAvatar(avatarUrl);
            int bool = webUserRepository.update(dto);
            if(bool != 1){
                return false;
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }

}

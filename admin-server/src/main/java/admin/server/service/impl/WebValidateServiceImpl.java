package admin.server.service.impl;
import admin.server.service.IWebValidateService;
import lombok.extern.slf4j.Slf4j;
import admin.server.dto.WebUserDTO;
import admin.server.repository.WebUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;


@Slf4j
@Service
public class WebValidateServiceImpl implements IWebValidateService {
    @Autowired
    private WebUserRepository webUserRepository;

    @Override
    public boolean checkUniqueUsername(String Username){
        Map<String, WebUserDTO> webUserDTOMap = webUserRepository.getMap();
        if(!webUserDTOMap.containsKey(Username)){
            return true;
        }
        else{
            return false;
        }
    }

    @Override
    public boolean checkUniqueDisplayName(String DisplayName){
        Map<String, WebUserDTO> webUserDTOMap = webUserRepository.getMapByName();
        if(!webUserDTOMap.containsKey(DisplayName)){
            return true;
        }
        else{
            return false;
        }
    }
    @Override
    public boolean checkOldPassword(String password, long id) {
        Map<Long, WebUserDTO> webUserDTOMap = webUserRepository.getMapById();
        if (webUserDTOMap.get(id).getPassword().equals(password)) {
            return true;
        } else {
            return false;
        }
    }


}

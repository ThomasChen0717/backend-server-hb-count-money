package logic.server.service;

import logic.server.config.NacosConfiguration;
import logic.server.dto.UsersDTO;
import logic.server.repository.UsersRepository;
import logic.server.repository.redis.RedisRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author mark
 * @date 2023-04-09
 */
@Slf4j
@Service
@AllArgsConstructor
public class LoginService {
    @Autowired
    private NacosConfiguration nacosConfiguration;

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private RedisRepository redisRepository;

    public void helloSpring() {
        Long userId = 1L;
        UsersDTO usersDTO = usersRepository.get(userId);
        if(usersDTO == null){
            UsersDTO newUsersDTO = new UsersDTO();
            newUsersDTO.setName("mark");
            usersRepository.add(newUsersDTO);
        }else{
            usersDTO.setName("mark-2");
            usersRepository.update(usersDTO);
        }

        log.info("LoginService::helloSpring");
    }
}
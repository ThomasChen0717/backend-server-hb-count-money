package logic.server.service.impl;

import logic.server.dto.UserDTO;
import logic.server.repository.UserRepository;
import logic.server.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserServiceImpl implements IUserService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public int addUser(UserDTO userDTO){
        return userRepository.add(userDTO);
    }

    @Override
    public UserDTO getUserById(long id){
        return userRepository.get(id);
    }

    @Override
    public UserDTO getUserByUnionId(String unionId){
        return userRepository.getByUnionId(unionId);
    }

    @Override
    public UserDTO getUserByToken(String unionId){
        return userRepository.getByToken(unionId);
    }

    @Override
    public int updateUser(UserDTO userDTO){
        return userRepository.update(userDTO);
    }
}

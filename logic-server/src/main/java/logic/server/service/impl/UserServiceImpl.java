package logic.server.service.impl;

import logic.server.dto.UserAttributeDTO;
import logic.server.dto.UserDTO;
import logic.server.dto.UserVehicleDTO;
import logic.server.repository.UserAttributeRepository;
import logic.server.repository.UserRepository;
import logic.server.repository.UserVehicleRepository;
import logic.server.repository.redis.RedisRepository;
import logic.server.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
public class UserServiceImpl implements IUserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserAttributeRepository userAttributeRepository;
    @Autowired
    private UserVehicleRepository userVehicleRepository;

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

    @Override
    public int addUserAttribute(UserAttributeDTO userAttributeDTO){
        return userAttributeRepository.add(userAttributeDTO);
    }
    @Override
    public UserAttributeDTO getUserAttributeById(long userId){
        return userAttributeRepository.get(userId);
    }

    @Override
    public int addUserVehicle(UserVehicleDTO userVehicleDTO){
        return userVehicleRepository.add(userVehicleDTO);
    }
    @Override
    public Map<Integer,UserVehicleDTO> getUserVehicleMapById(long userId){
        return userVehicleRepository.getMap(userId);
    }
}

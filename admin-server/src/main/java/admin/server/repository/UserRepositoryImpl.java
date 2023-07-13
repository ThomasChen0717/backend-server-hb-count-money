package admin.server.repository;

import admin.server.dto.UserCountDTO;
import admin.server.po.UserCountPO;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import admin.server.dto.UserDTO;
import admin.server.mapper.UserMapper;
import admin.server.po.UserPO;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Repository
@AllArgsConstructor
public class UserRepositoryImpl implements admin.server.repository.UserRepository {
    private final UserMapper userMapper;
    @Override
    public Map<Long, UserDTO> getMap() {
        List<UserPO> userPOList = userMapper.selectList(new QueryWrapper<UserPO>()
                .lambda());
        List<UserDTO> userDTOListDTOList = Convertor.convert(userPOList, UserDTO.class);
        Map<Long, UserDTO> userDTOMap = userDTOListDTOList.stream().collect(Collectors.toMap(UserDTO::getId, UserDTO -> UserDTO));
        return userDTOMap;
    }
}

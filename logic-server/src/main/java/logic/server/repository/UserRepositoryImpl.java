package logic.server.repository;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import logic.server.dto.UserDTO;
import logic.server.mapper.UserMapper;
import logic.server.po.UserPO;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
@AllArgsConstructor
public class UserRepositoryImpl implements UserRepository {
    private final UserMapper userMapper;

    @Override
    public int add(UserDTO dto) {
        UserPO po = Convertor.convert(UserPO.class, dto);
        int result = userMapper.insert(po);
        dto.setId(po.getId());
        return result;
    }

    @Override
    public int update(UserDTO dto){
        UserPO po = Convertor.convert(UserPO.class, dto);
        return userMapper.update(po, new QueryWrapper<UserPO>()
                .lambda().eq(UserPO::getId, po.getId()));
    }

    @Override
    public UserDTO get(long userId){
        UserPO po = userMapper.selectOne(new QueryWrapper<UserPO>()
                .lambda().eq(UserPO::getId, userId));
        return Convertor.convert(UserDTO.class, po);
    }

    @Override
    public UserDTO getByUnionId(String unionId){
        UserPO po = userMapper.selectOne(new QueryWrapper<UserPO>()
                .lambda().eq(UserPO::getUnionId, unionId));
        return Convertor.convert(UserDTO.class, po);
    }

    @Override
    public UserDTO getByToken(String token){
        UserPO po = userMapper.selectOne(new QueryWrapper<UserPO>()
                .lambda().eq(UserPO::getToken, token));
        return Convertor.convert(UserDTO.class, po);
    }

    @Override
    public void checkUserOnlineServerId(int onlineServerId){
        userMapper.updateOnlineServerId(onlineServerId);
    }
    @Override
    public void updateOnlineServerIdById(long userId,int onlineServerId){
        userMapper.updateOnlineServerIdById(userId,onlineServerId);
    }
}

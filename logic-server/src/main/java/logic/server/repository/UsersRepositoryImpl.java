package logic.server.repository;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import logic.server.dto.UsersDTO;
import logic.server.mapper.UsersMapper;
import logic.server.po.UsersPO;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
@AllArgsConstructor
public class UsersRepositoryImpl implements UsersRepository{
    private final UsersMapper usersMapper;

    @Override
    public Integer add(UsersDTO dto) {
        UsersPO po = Convertor.convert(UsersPO.class, dto);
        return usersMapper.insert(po);
    }

    @Override
    public UsersDTO get(Long userId){
        UsersPO po = usersMapper.selectOne(new QueryWrapper<UsersPO>()
                .lambda().eq(UsersPO::getId, userId));
        return Convertor.convert(UsersDTO.class, po);
    }

    @Override
    public Integer update(UsersDTO dto){
        UsersPO po = Convertor.convert(UsersPO.class, dto);
        return usersMapper.update(po, new QueryWrapper<UsersPO>()
                .lambda().eq(UsersPO::getId, po.getId()));
    }
}

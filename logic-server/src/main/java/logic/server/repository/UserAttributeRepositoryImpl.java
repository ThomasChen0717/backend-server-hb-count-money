package logic.server.repository;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import logic.server.dto.UserAttributeDTO;
import logic.server.mapper.UserAttributeMapper;
import logic.server.po.UserAttributePO;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
@AllArgsConstructor
public class UserAttributeRepositoryImpl implements UserAttributeRepository{
    private final UserAttributeMapper userAttributeMapper;
    @Override
    public int add(UserAttributeDTO dto) {
        UserAttributePO po = Convertor.convert(UserAttributePO.class, dto);
        int result = userAttributeMapper.insert(po);
        dto.setId(po.getId());
        return result;
    }

    @Override
    public int update(UserAttributeDTO dto){
        UserAttributePO po = Convertor.convert(UserAttributePO.class, dto);
        return userAttributeMapper.update(po, new QueryWrapper<UserAttributePO>()
                .lambda().eq(UserAttributePO::getUserId, po.getUserId()));
    }

    @Override
    public UserAttributeDTO get(long userId){
        UserAttributePO po = userAttributeMapper.selectOne(new QueryWrapper<UserAttributePO>()
                .lambda().eq(UserAttributePO::getUserId, userId));
        return Convertor.convert(UserAttributeDTO.class, po);
    }
}

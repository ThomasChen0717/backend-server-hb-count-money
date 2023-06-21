package logic.server.repository;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import logic.server.dto.UserVipDTO;
import logic.server.mapper.UserVipMapper;
import logic.server.po.UserMagnatePO;
import logic.server.po.UserVipPO;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
@AllArgsConstructor
public class UserVipRepositoryImpl implements UserVipRepository{
    private final UserVipMapper userVipMapper;
    @Override
    public int add(UserVipDTO dto) {
        UserVipPO po = Convertor.convert(UserVipPO.class, dto);
        int result = userVipMapper.insert(po);
        dto.setId(po.getId());
        return result;
    }

    @Override
    public int update(UserVipDTO dto){
        UserVipPO po = Convertor.convert(UserVipPO.class, dto);
        return userVipMapper.update(po, new QueryWrapper<UserVipPO>()
                .lambda().eq(UserVipPO::getUserId, po.getUserId()));
    }

    @Override
    public UserVipDTO get(long userId){
        UserVipPO po = userVipMapper.selectOne(new QueryWrapper<UserVipPO>()
                .lambda().eq(UserVipPO::getUserId, userId));
        return Convertor.convert(UserVipDTO.class, po);
    }

    @Override
    public void deleteByUserId(long userId){
        userVipMapper.delete(new QueryWrapper<UserVipPO>()
                .lambda()
                .eq(UserVipPO::getUserId, userId)
        );
    }
}

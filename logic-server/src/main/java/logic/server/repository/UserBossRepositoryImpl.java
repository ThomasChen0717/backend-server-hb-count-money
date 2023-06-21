package logic.server.repository;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import logic.server.dto.UserBossDTO;
import logic.server.mapper.UserBossMapper;
import logic.server.po.UserAttributePO;
import logic.server.po.UserBossPO;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Repository
@AllArgsConstructor
public class UserBossRepositoryImpl implements UserBossRepository{
    private final UserBossMapper userBossMapper;
    @Override
    public int add(UserBossDTO dto) {
        UserBossPO po = Convertor.convert(UserBossPO.class, dto);
        int result = userBossMapper.insert(po);
        dto.setId(po.getId());
        return result;
    }

    @Override
    public int update(UserBossDTO dto){
        UserBossPO po = Convertor.convert(UserBossPO.class, dto);
        return userBossMapper.update(po, new QueryWrapper<UserBossPO>()
                .lambda().eq(UserBossPO::getId, po.getId()));
    }

    @Override
    public Map<Integer, UserBossDTO> getMap(long userId){
        List<UserBossPO> cfgBossPOList = userBossMapper.selectList(new QueryWrapper<UserBossPO>()
                .lambda()
                .eq(UserBossPO::getUserId,userId)
        );
        List<UserBossDTO> bossDTOList = Convertor.convert(cfgBossPOList, UserBossDTO.class);
        Map<Integer, UserBossDTO> cfgBossDTOmap = bossDTOList.stream().collect(Collectors.toMap(UserBossDTO::getBossId, UserBossDTO -> UserBossDTO));
        return cfgBossDTOmap;
    }

    @Override
    public void deleteByUserId(long userId){
        userBossMapper.delete(new QueryWrapper<UserBossPO>()
                .lambda()
                .eq(UserBossPO::getUserId, userId)
        );
    }
}

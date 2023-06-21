package logic.server.repository;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import logic.server.dto.UserMagnateDTO;
import logic.server.mapper.UserMagnateMapper;
import logic.server.po.UserBossPO;
import logic.server.po.UserMagnatePO;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Repository
@AllArgsConstructor
public class UserMagnateRepositoryImpl implements UserMagnateRepository{
    private final UserMagnateMapper userMagnateMapper;
    @Override
    public int add(UserMagnateDTO dto) {
        UserMagnatePO po = Convertor.convert(UserMagnatePO.class, dto);
        int result = userMagnateMapper.insert(po);
        dto.setId(po.getId());
        return result;
    }

    @Override
    public int update(UserMagnateDTO dto){
        UserMagnatePO po = Convertor.convert(UserMagnatePO.class, dto);
        return userMagnateMapper.update(po, new QueryWrapper<UserMagnatePO>()
                .lambda().eq(UserMagnatePO::getId, po.getId()));
    }

    @Override
    public Map<Integer, UserMagnateDTO> getMap(long userId){
        List<UserMagnatePO> cfgMagnatePOList = userMagnateMapper.selectList(new QueryWrapper<UserMagnatePO>()
                .lambda()
                .eq(UserMagnatePO::getUserId,userId)
        );
        List<UserMagnateDTO> magnateDTOList = Convertor.convert(cfgMagnatePOList, UserMagnateDTO.class);
        Map<Integer, UserMagnateDTO> cfgMagnateDTOmap = magnateDTOList.stream().collect(Collectors.toMap(UserMagnateDTO::getMagnateId, UserMagnateDTO -> UserMagnateDTO));
        return cfgMagnateDTOmap;
    }

    @Override
    public void deleteByUserId(long userId){
        userMagnateMapper.delete(new QueryWrapper<UserMagnatePO>()
                .lambda()
                .eq(UserMagnatePO::getUserId, userId)
        );
    }
}

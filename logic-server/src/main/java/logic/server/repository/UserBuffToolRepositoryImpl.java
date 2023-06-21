package logic.server.repository;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import logic.server.dto.UserBuffToolDTO;
import logic.server.mapper.UserBuffToolMapper;
import logic.server.po.UserBossPO;
import logic.server.po.UserBuffToolPO;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Repository
@AllArgsConstructor

public class UserBuffToolRepositoryImpl implements UserBuffToolRepository{
    private final UserBuffToolMapper userBuffToolMapper;
    @Override
    public int add(UserBuffToolDTO dto) {
        UserBuffToolPO po = Convertor.convert(UserBuffToolPO.class, dto);
        int result = userBuffToolMapper.insert(po);
        dto.setId(po.getId());
        return result;
    }

    @Override
    public int update(UserBuffToolDTO dto){
        UserBuffToolPO po = Convertor.convert(UserBuffToolPO.class, dto);
        return userBuffToolMapper.update(po, new QueryWrapper<UserBuffToolPO>()
                .lambda().eq(UserBuffToolPO::getId, po.getId()));
    }

    @Override
    public Map<Integer, UserBuffToolDTO> getMap(long userId){
        List<UserBuffToolPO> cfgBuffToolPOList = userBuffToolMapper.selectList(new QueryWrapper<UserBuffToolPO>()
                .lambda()
                .eq(UserBuffToolPO::getUserId,userId)
        );
        List<UserBuffToolDTO> buffToolDTOList = Convertor.convert(cfgBuffToolPOList, UserBuffToolDTO.class);
        Map<Integer, UserBuffToolDTO> cfgBuffToolDTOmap = buffToolDTOList.stream().collect(Collectors.toMap(UserBuffToolDTO::getBuffToolId, UserBuffToolDTO -> UserBuffToolDTO));
        return cfgBuffToolDTOmap;
    }

    @Override
    public void deleteByUserId(long userId){
        userBuffToolMapper.delete(new QueryWrapper<UserBuffToolPO>()
                .lambda()
                .eq(UserBuffToolPO::getUserId, userId)
        );
    }
}

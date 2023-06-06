package logic.server.repository;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import logic.server.dto.CfgAttributeDTO;
import logic.server.dto.CfgBossDTO;
import logic.server.mapper.CfgBossMapper;
import logic.server.po.CfgAttributePO;
import logic.server.po.CfgBossPO;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Repository
@AllArgsConstructor
public class CfgBossRepositoryImpl implements CfgBossRepository{
    private final CfgBossMapper cfgBossMapper;

    @Override
    public Map<Integer, CfgBossDTO> getMap(){
        List<CfgBossPO> cfgBossPOList = cfgBossMapper.selectList(new QueryWrapper<CfgBossPO>()
                .lambda());
        List<CfgBossDTO> cfgBossDTOListDTOList = Convertor.convert(cfgBossPOList, CfgBossDTO.class);
        Map<Integer, CfgBossDTO> cfgBossDTOMap = cfgBossDTOListDTOList.stream().collect(Collectors.toMap(CfgBossDTO::getBossId, CfgBossDTO -> CfgBossDTO));
        return cfgBossDTOMap;
    }

    @Override
    public int add(CfgBossDTO dto) {
        CfgBossPO po = Convertor.convert(CfgBossPO.class, dto);
        int result = cfgBossMapper.insert(po);
        dto.setId(po.getId());
        return result;
    }

    @Override
    public int delete(){
        return cfgBossMapper.delete(new QueryWrapper<CfgBossPO>().lambda());
    }
}

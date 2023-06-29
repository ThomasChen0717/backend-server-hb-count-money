package logic.server.repository;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import logic.server.dto.CfgDrawDTO;
import logic.server.mapper.CfgDrawMapper;
import logic.server.po.CfgDrawPO;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Repository
@AllArgsConstructor
public class CfgDrawRepositoryImpl implements CfgDrawRepository{
    private final CfgDrawMapper cfgDrawMapper;
    @Override
    public Map<Integer, CfgDrawDTO> getMap(){
        List<CfgDrawPO> cfgDrawPOList = cfgDrawMapper.selectList(new QueryWrapper<CfgDrawPO>()
                .lambda());
        List<CfgDrawDTO> cfgDrawDTOList = Convertor.convert(cfgDrawPOList, CfgDrawDTO.class);
        Map<Integer, CfgDrawDTO> cfgDrawDTOMap = cfgDrawDTOList.stream().collect(Collectors.toMap(CfgDrawDTO::getRoundNumber, CfgDrawDTO -> CfgDrawDTO));
        return cfgDrawDTOMap;
    }
}

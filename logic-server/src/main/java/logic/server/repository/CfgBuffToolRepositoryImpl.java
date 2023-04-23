package logic.server.repository;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import logic.server.dto.CfgBuffToolDTO;
import logic.server.mapper.CfgBuffToolMapper;
import logic.server.po.CfgBuffToolPO;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Repository
@AllArgsConstructor
public class CfgBuffToolRepositoryImpl implements CfgBuffToolRepository{
    private final CfgBuffToolMapper cfgBuffToolMapper;

    @Override
    public Map<Integer, CfgBuffToolDTO> getMap(){
        List<CfgBuffToolPO> cfgBuffToolPOList = cfgBuffToolMapper.selectList(new QueryWrapper<CfgBuffToolPO>()
                .lambda());
        List<CfgBuffToolDTO> cfgBuffToolDTOListDTOList = Convertor.convert(cfgBuffToolPOList, CfgBuffToolDTO.class);
        Map<Integer, CfgBuffToolDTO> cfgBuffToolDTOMap = cfgBuffToolDTOListDTOList.stream().collect(Collectors.toMap(CfgBuffToolDTO::getBuffToolId, CfgBuffToolDTO -> CfgBuffToolDTO));
        return cfgBuffToolDTOMap;
    }
}

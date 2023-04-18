package logic.server.repository;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import logic.server.dto.CfgAttributeDTO;
import logic.server.mapper.CfgAttributeMapper;
import logic.server.po.CfgAttributePO;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Repository
@AllArgsConstructor
public class CfgAttributeRepositoryImpl implements CfgAttributeRepository{
    private final CfgAttributeMapper cfgAttributeMapper;

    @Override
    public Map<Integer, CfgAttributeDTO> getMap(){
        List<CfgAttributePO> cfgAttributePOList = cfgAttributeMapper.selectList(new QueryWrapper<CfgAttributePO>()
                .lambda());
        List<CfgAttributeDTO> cfgAttributeDTOListDTOList = Convertor.convert(cfgAttributePOList, CfgAttributeDTO.class);
        Map<Integer, CfgAttributeDTO> cfgAttributeDTOMap = cfgAttributeDTOListDTOList.stream().collect(Collectors.toMap(CfgAttributeDTO::getAttributeType, CfgAttributeDTO -> CfgAttributeDTO));
        return cfgAttributeDTOMap;
    }
}

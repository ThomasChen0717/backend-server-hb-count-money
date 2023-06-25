package admin.server.repository;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import admin.server.dto.CfgAttributeDTO;
import admin.server.mapper.CfgAttributeMapper;
import admin.server.po.CfgAttributePO;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Repository
@AllArgsConstructor
public class CfgAttributeRepositoryImpl implements CfgAttributeRepository {
    private final CfgAttributeMapper cfgAttributeMapper;

    @Override
    public Map<Integer, CfgAttributeDTO> getMap() {
        List<CfgAttributePO> cfgAttributePOList = cfgAttributeMapper.selectList(new QueryWrapper<CfgAttributePO>()
                .lambda());
        List<CfgAttributeDTO> cfgAttributeDTOListDTOList = Convertor.convert(cfgAttributePOList, CfgAttributeDTO.class);
        Map<Integer, CfgAttributeDTO> cfgAttributeDTOMap = cfgAttributeDTOListDTOList.stream().collect(Collectors.toMap(CfgAttributeDTO::getAttributeType, CfgAttributeDTO -> CfgAttributeDTO));
        return cfgAttributeDTOMap;
    }

    @Override
    public int add(CfgAttributeDTO dto) {
        CfgAttributePO po = Convertor.convert(CfgAttributePO.class, dto);
        int result = cfgAttributeMapper.insert(po);
        dto.setId(po.getId());
        return result;
    }

    @Override
    public int delete(){
        return cfgAttributeMapper.delete(new QueryWrapper<CfgAttributePO>().lambda());
    }
}

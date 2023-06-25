package admin.server.repository;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import admin.server.dto.CfgMagnateDTO;
import admin.server.mapper.CfgMagnateMapper;
import admin.server.po.CfgMagnatePO;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Repository
@AllArgsConstructor
public class CfgMagnateRepositoryImpl implements CfgMagnateRepository {
    private final CfgMagnateMapper cfgMagnateMapper;

    @Override
    public Map<Integer, CfgMagnateDTO> getMap(){
        List<CfgMagnatePO> cfgMagnatePOList = cfgMagnateMapper.selectList(new QueryWrapper<CfgMagnatePO>()
                .lambda());
        List<CfgMagnateDTO> cfgMagnateDTOListDTOList = Convertor.convert(cfgMagnatePOList, CfgMagnateDTO.class);
        Map<Integer, CfgMagnateDTO> cfgMagnateDTOMap = cfgMagnateDTOListDTOList.stream().collect(Collectors.toMap(CfgMagnateDTO::getMagnateId, CfgMagnateDTO -> CfgMagnateDTO));
        return cfgMagnateDTOMap;
    }

    @Override
    public int add(CfgMagnateDTO dto) {
        CfgMagnatePO po = Convertor.convert(CfgMagnatePO.class, dto);
        int result = cfgMagnateMapper.insert(po);
        dto.setId(po.getId());
        return result;
    }

    @Override
    public int delete(){
        return cfgMagnateMapper.delete(new QueryWrapper<CfgMagnatePO>().lambda());
    }
}

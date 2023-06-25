package admin.server.repository;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import admin.server.dto.CfgGlobalDTO;
import admin.server.mapper.CfgGlobalMapper;
import admin.server.po.CfgGlobalPO;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Repository
@AllArgsConstructor
public class CfgGlobalRepositoryImpl implements CfgGlobalRepository {
    private CfgGlobalMapper cfgGlobalMapper;

    @Override
    public Map<String,CfgGlobalDTO> getMap(){
        List<CfgGlobalPO> cfgGlobalPOList = cfgGlobalMapper.selectList(new QueryWrapper<CfgGlobalPO>()
                .lambda());
        List<CfgGlobalDTO> cfgGlobalDTOList = Convertor.convert(cfgGlobalPOList, CfgGlobalDTO.class);
        Map<String, CfgGlobalDTO> cfgGlobalDTOmap = cfgGlobalDTOList.stream().collect(Collectors.toMap(CfgGlobalDTO::getKeyName, CfgGlobalDTO -> CfgGlobalDTO));
        return cfgGlobalDTOmap;
    }

    @Override
    public int add(CfgGlobalDTO dto) {
        CfgGlobalPO po = Convertor.convert(CfgGlobalPO.class, dto);
        int result = cfgGlobalMapper.insert(po);
        dto.setId(po.getId());
        return result;
    }

    @Override
    public int delete(){
        return cfgGlobalMapper.delete(new QueryWrapper<CfgGlobalPO>().lambda());
    }
}

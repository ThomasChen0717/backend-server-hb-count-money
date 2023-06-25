package admin.server.repository;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import admin.server.dto.CfgVehicleDTO;
import admin.server.mapper.CfgVehicleMapper;
import admin.server.po.CfgVehiclePO;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Repository
@AllArgsConstructor
public class CfgVehicleRepositoryImpl implements CfgVehicleRepository {
    private final CfgVehicleMapper cfgVehicleMapper;

    @Override
    public Map<Integer,CfgVehicleDTO> getMap(){
        List<CfgVehiclePO> cfgVehiclePOList = cfgVehicleMapper.selectList(new QueryWrapper<CfgVehiclePO>()
                .lambda());
        List<CfgVehicleDTO> cfgVehicleDTOList = Convertor.convert(cfgVehiclePOList, CfgVehicleDTO.class);
        Map<Integer, CfgVehicleDTO> cfgVehicleDTOmap = cfgVehicleDTOList.stream().collect(Collectors.toMap(CfgVehicleDTO::getVehicleId, CfgVehicleDTO -> CfgVehicleDTO));
        return cfgVehicleDTOmap;
    }

    @Override
    public int add(CfgVehicleDTO dto) {
        CfgVehiclePO po = Convertor.convert(CfgVehiclePO.class, dto);
        int result = cfgVehicleMapper.insert(po);
        dto.setId(po.getId());
        return result;
    }

    @Override
    public int delete(){
        return cfgVehicleMapper.delete(new QueryWrapper<CfgVehiclePO>().lambda());
    }
}

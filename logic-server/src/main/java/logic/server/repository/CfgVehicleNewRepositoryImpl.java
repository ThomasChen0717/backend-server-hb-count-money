package logic.server.repository;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import logic.server.dto.CfgVehicleNewDTO;
import logic.server.mapper.CfgVehicleNewMapper;
import logic.server.po.CfgVehicleNewPO;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Repository
@AllArgsConstructor
public class CfgVehicleNewRepositoryImpl implements CfgVehicleNewRepository{
    private final CfgVehicleNewMapper cfgVehicleNewMapper;
    @Override
    public Map<Integer, CfgVehicleNewDTO> getMap(){
        List<CfgVehicleNewPO> cfgVehicleNewPOList = cfgVehicleNewMapper.selectList(new QueryWrapper<CfgVehicleNewPO>()
                .lambda());
        List<CfgVehicleNewDTO> cfgVehicleNewDTOList = Convertor.convert(cfgVehicleNewPOList, CfgVehicleNewDTO.class);
        Map<Integer, CfgVehicleNewDTO> cfgVehicleNewDTOmap = cfgVehicleNewDTOList.stream().collect(Collectors.toMap(CfgVehicleNewDTO::getVehicleId, CfgVehicleNewDTO -> CfgVehicleNewDTO));
        return cfgVehicleNewDTOmap;
    }
}

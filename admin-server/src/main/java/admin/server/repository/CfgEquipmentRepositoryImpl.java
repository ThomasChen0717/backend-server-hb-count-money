package admin.server.repository;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import admin.server.dto.CfgEquipmentDTO;
import admin.server.mapper.CfgEquipmentMapper;
import admin.server.po.CfgEquipmentPO;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Repository
@AllArgsConstructor
public class CfgEquipmentRepositoryImpl implements CfgEquipmentRepository {
    private final CfgEquipmentMapper cfgEquipmentMapper;

    @Override
    public Map<Integer, CfgEquipmentDTO> getMap(){
        List<CfgEquipmentPO> cfgEquipmentPOList = cfgEquipmentMapper.selectList(new QueryWrapper<CfgEquipmentPO>()
                .lambda());
        List<CfgEquipmentDTO> cfgEquipmentDTOListDTOList = Convertor.convert(cfgEquipmentPOList, CfgEquipmentDTO.class);
        Map<Integer, CfgEquipmentDTO> cfgEquipmentDTOMap = cfgEquipmentDTOListDTOList.stream().collect(Collectors.toMap(CfgEquipmentDTO::getEquipmentId, CfgEquipmentDTO -> CfgEquipmentDTO));
        return cfgEquipmentDTOMap;
    }

    @Override
    public int add(CfgEquipmentDTO dto) {
        CfgEquipmentPO po = Convertor.convert(CfgEquipmentPO.class, dto);
        int result = cfgEquipmentMapper.insert(po);
        dto.setId(po.getId());
        return result;
    }

    @Override
    public int delete(){
        return cfgEquipmentMapper.delete(new QueryWrapper<CfgEquipmentPO>().lambda());
    }
}

package logic.server.repository;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import logic.server.dto.CfgVipDTO;
import logic.server.mapper.CfgVipMapper;
import logic.server.po.CfgVipPO;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Repository
@AllArgsConstructor
public class CfgVipRepositoryImpl implements CfgVipRepository{
    private final CfgVipMapper cfgVipMapper;

    @Override
    public Map<Integer, CfgVipDTO> getMap(){
        List<CfgVipPO> cfgVipPOList = cfgVipMapper.selectList(new QueryWrapper<CfgVipPO>()
                .lambda());
        List<CfgVipDTO> cfgVipDTOListDTOList = Convertor.convert(cfgVipPOList, CfgVipDTO.class);
        Map<Integer, CfgVipDTO> cfgVipDTOMap = cfgVipDTOListDTOList.stream().collect(Collectors.toMap(CfgVipDTO::getVipLevel, CfgVipDTO -> CfgVipDTO));
        return cfgVipDTOMap;
    }
}

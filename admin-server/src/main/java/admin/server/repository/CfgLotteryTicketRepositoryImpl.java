package admin.server.repository;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import admin.server.dto.CfgLotteryTicketDTO;
import admin.server.mapper.CfgLotteryTicketMapper;
import admin.server.po.CfgLotteryTicketPO;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Repository
@AllArgsConstructor
public class CfgLotteryTicketRepositoryImpl implements CfgLotteryTicketRepository {
    private final CfgLotteryTicketMapper cfgLotteryTicketMapper;

    @Override
    public Map<Integer, CfgLotteryTicketDTO> getMap(){
        List<CfgLotteryTicketPO> cfgLotteryTicketPOList = cfgLotteryTicketMapper.selectList(new QueryWrapper<CfgLotteryTicketPO>()
                .lambda());
        List<CfgLotteryTicketDTO> cfgLotteryTicketDTOList = Convertor.convert(cfgLotteryTicketPOList, CfgLotteryTicketDTO.class);
        Map<Integer, CfgLotteryTicketDTO> cfgLotteryTicketDTOMap = cfgLotteryTicketDTOList.stream().collect(Collectors.toMap(CfgLotteryTicketDTO::getFaceValue, CfgLotteryTicketDTO -> CfgLotteryTicketDTO));
        return cfgLotteryTicketDTOMap;
    }

    @Override
    public int add(CfgLotteryTicketDTO dto) {
        CfgLotteryTicketPO po = Convertor.convert(CfgLotteryTicketPO.class, dto);
        int result = cfgLotteryTicketMapper.insert(po);
        dto.setId(po.getId());
        return result;
    }

    @Override
    public int delete(){
        return cfgLotteryTicketMapper.delete(new QueryWrapper<CfgLotteryTicketPO>().lambda());
    }
}

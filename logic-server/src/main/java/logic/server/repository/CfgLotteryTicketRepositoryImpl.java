package logic.server.repository;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import logic.server.dto.CfgLotteryTicketDTO;
import logic.server.mapper.CfgLotteryTicketMapper;
import logic.server.po.CfgLotteryTicketPO;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Repository
@AllArgsConstructor
public class CfgLotteryTicketRepositoryImpl implements CfgLotteryTicketRepository{
    private final CfgLotteryTicketMapper cfgLotteryTicketMapper;

    @Override
    public Map<Integer, CfgLotteryTicketDTO> getMap(){
        List<CfgLotteryTicketPO> cfgLotteryTicketPOList = cfgLotteryTicketMapper.selectList(new QueryWrapper<CfgLotteryTicketPO>()
                .lambda());
        List<CfgLotteryTicketDTO> cfgLotteryTicketDTOList = Convertor.convert(cfgLotteryTicketPOList, CfgLotteryTicketDTO.class);
        Map<Integer, CfgLotteryTicketDTO> cfgLotteryTicketDTOMap = cfgLotteryTicketDTOList.stream().collect(Collectors.toMap(CfgLotteryTicketDTO::getFaceValue, CfgLotteryTicketDTO -> CfgLotteryTicketDTO));
        return cfgLotteryTicketDTOMap;
    }
}

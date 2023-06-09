package logic.server.repository;

import logic.server.dto.CfgLotteryTicketDTO;

import java.util.Map;

public interface CfgLotteryTicketRepository {
    Map<Integer, CfgLotteryTicketDTO> getMap();
}

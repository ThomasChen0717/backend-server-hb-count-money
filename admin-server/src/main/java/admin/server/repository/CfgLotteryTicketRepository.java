package admin.server.repository;

import admin.server.dto.CfgLotteryTicketDTO;

import java.util.Map;

public interface CfgLotteryTicketRepository {
    Map<Integer, CfgLotteryTicketDTO> getMap();

    int delete();

    int add(CfgLotteryTicketDTO dto);
}

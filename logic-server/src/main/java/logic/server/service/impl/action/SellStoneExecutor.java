package logic.server.service.impl.action;

import common.pb.pb.SellStoneReqPb;
import common.pb.pb.SellStoneResPb;
import logic.server.dto.UserDTO;
import logic.server.service.IPushPbService;
import logic.server.singleton.UserManagerSingleton;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class SellStoneExecutor implements BaseExecutor<SellStoneReqPb, SellStoneResPb,Long>{
    @Autowired
    private IPushPbService pushPbService;
    @Override
    public SellStoneResPb executor(SellStoneReqPb arg, Long userId){
        log.info("SellStoneExecutor::executor:userId = {},arg = {},start",userId,arg);
        SellStoneResPb sellStoneResPb = new SellStoneResPb();

        UserDTO userDTO = UserManagerSingleton.getInstance().getUserByIdFromCache(userId);
        long finalMoney = userDTO.getMoney() + arg.getSellStoneMoney();
        userDTO.setMoney(finalMoney);

        /** 同步金钱数量（推送）**/
        pushPbService.moneySync(userId);

        log.info("SellStoneExecutor::executor:userId = {},sellStoneResPb = {},end",userId,sellStoneResPb);
        return sellStoneResPb;
    }
}

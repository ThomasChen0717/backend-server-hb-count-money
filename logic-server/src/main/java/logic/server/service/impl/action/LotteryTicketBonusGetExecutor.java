package logic.server.service.impl.action;

import com.iohao.game.action.skeleton.core.exception.MsgException;
import common.pb.pb.LotteryTicketBonusGetReqPb;
import common.pb.pb.LotteryTicketBonusGetResPb;
import logic.server.dto.UserDTO;
import logic.server.service.IPushPbService;
import logic.server.singleton.UserManagerSingleton;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class LotteryTicketBonusGetExecutor implements BaseExecutor<LotteryTicketBonusGetReqPb, LotteryTicketBonusGetResPb,Long>{
    @Autowired
    private IPushPbService pushPbService;

    @Override
    public LotteryTicketBonusGetResPb executor(LotteryTicketBonusGetReqPb arg, Long userId) throws MsgException {
        log.info("LotteryTicketBonusGetExecutor::executor:userId = {},arg = {},start",userId,arg);
        LotteryTicketBonusGetResPb lotteryTicketBonusGetResPb = new LotteryTicketBonusGetResPb();

        if(arg.getBonus() > 0){
            UserDTO userDTO = UserManagerSingleton.getInstance().getUserByIdFromCache(userId);
            long finalMoney = userDTO.getMoney() + arg.getBonus();
            userDTO.setMoney(finalMoney);
            /** 同步金钱数量（推送）**/
            pushPbService.moneySync(userId);
        }

        log.info("LotteryTicketBonusGetExecutor::executor:userId = {},lotteryTicketBonusGetResPb = {},end",userId, lotteryTicketBonusGetResPb);
        return lotteryTicketBonusGetResPb;
    }
}

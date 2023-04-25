package logic.server.service.impl.action;

import common.pb.enums.ErrorCodeEnum;
import common.pb.pb.ChallengeBossSuccessReqPb;
import common.pb.pb.ChallengeBossSuccessResPb;
import logic.server.dto.CfgBossDTO;
import logic.server.dto.UserBossDTO;
import logic.server.dto.UserDTO;
import logic.server.service.IPushPbService;
import logic.server.singleton.CfgManagerSingleton;
import logic.server.singleton.UserManagerSingleton;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ChallengeBossSuccessExecutor implements BaseExecutor<ChallengeBossSuccessReqPb, ChallengeBossSuccessResPb,Long> {
    @Autowired
    private IPushPbService pushPbService;
    @Override
    public ChallengeBossSuccessResPb executor(ChallengeBossSuccessReqPb arg, Long userId){
        log.info("ChallengeBossSuccessExecutor::executor:userId = {},start",userId);

        UserDTO userDTO = UserManagerSingleton.getInstance().getUserByIdFromCache(userId);
        ErrorCodeEnum.userNotExist.assertNonNull(userDTO);
        UserBossDTO userBossDTO = UserManagerSingleton.getInstance().getUserBossByIdFromCache(userId,arg.getBossId());
        ErrorCodeEnum.bossNotExist.assertNonNull(userBossDTO);
        ErrorCodeEnum.bossIsLock.assertTrue(userBossDTO.isUnlocked());
        CfgBossDTO cfgBossDTO = CfgManagerSingleton.getInstance().getCfgBossByIdFromCache(arg.getBossId());

        long moneyIncome = arg.getMultiple() * cfgBossDTO.getRewardMoneyAmount();
        long finalMoney = userDTO.getMoney() + moneyIncome;
        userDTO.setMoney(finalMoney);

        /** 同步金钱数量（推送）**/
        pushPbService.moneySync(userId);

        ChallengeBossSuccessResPb challengeBossSuccessResPb = new ChallengeBossSuccessResPb();
        // 解锁下一个boss
        CfgBossDTO nextCfgBossDTO = CfgManagerSingleton.getInstance().getNextCfgBossByIdFromCache(arg.getBossId());
        if(nextCfgBossDTO != null){
            UserBossDTO nextUserBossDTO = UserManagerSingleton.getInstance().getUserBossByIdFromCache(userId,nextCfgBossDTO.getBossId());
            nextUserBossDTO.setUnlocked(true);
            challengeBossSuccessResPb.setNextBossId(nextCfgBossDTO.getBossId());
        }

        log.info("ChallengeBossSuccessExecutor::executor:userId = {},challengeBossSuccessResPb = {},end",userId,challengeBossSuccessResPb);
        return challengeBossSuccessResPb;
    }
}

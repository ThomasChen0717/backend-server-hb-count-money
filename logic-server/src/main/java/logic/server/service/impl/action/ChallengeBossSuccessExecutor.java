package logic.server.service.impl.action;

import common.pb.enums.ErrorCodeEnum;
import common.pb.pb.ChallengeBossSuccessReqPb;
import common.pb.pb.ChallengeBossSuccessResPb;
import logic.server.dto.CfgBossDTO;
import logic.server.dto.CfgVehicleNewDTO;
import logic.server.dto.UserBossDTO;
import logic.server.dto.UserDTO;
import logic.server.dto.UserVehicleNewDTO;
import logic.server.service.IPushPbService;
import logic.server.singleton.CfgManagerSingleton;
import logic.server.singleton.UserManagerSingleton;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
public class ChallengeBossSuccessExecutor implements BaseExecutor<ChallengeBossSuccessReqPb, ChallengeBossSuccessResPb,Long> {
    @Autowired
    private IPushPbService pushPbService;
    @Override
    public ChallengeBossSuccessResPb executor(ChallengeBossSuccessReqPb arg, Long userId){
        log.info("ChallengeBossSuccessExecutor::executor:userId = {},arg = {},start",userId,arg);
        ChallengeBossSuccessResPb challengeBossSuccessResPb = new ChallengeBossSuccessResPb();

        UserDTO userDTO = UserManagerSingleton.getInstance().getUserByIdFromCache(userId);
        UserBossDTO userBossDTO = UserManagerSingleton.getInstance().getUserBossByIdFromCache(userId,arg.getBossId());

        if(userBossDTO == null){
            challengeBossSuccessResPb.setCode(ErrorCodeEnum.bossNotExist.getCode()).setMessage(ErrorCodeEnum.bossNotExist.getMsg());
            log.info("ChallengeBossSuccessExecutor::executor:userId = {},challengeBossSuccessResPb = {},end",userId,challengeBossSuccessResPb);
            return challengeBossSuccessResPb;
        }
        ErrorCodeEnum.bossIsLock.assertTrue(userBossDTO.isUnlocked());
        if(!userBossDTO.isUnlocked()){
            challengeBossSuccessResPb.setCode(ErrorCodeEnum.bossIsLock.getCode()).setMessage(ErrorCodeEnum.bossIsLock.getMsg());
            log.info("ChallengeBossSuccessExecutor::executor:userId = {},challengeBossSuccessResPb = {},end",userId,challengeBossSuccessResPb);
            return challengeBossSuccessResPb;
        }
        userBossDTO.setBeat(true);

        CfgBossDTO cfgBossDTO = CfgManagerSingleton.getInstance().getCfgBossByIdFromCache(arg.getBossId());
        long moneyIncome = arg.getMultiple() * cfgBossDTO.getRewardMoneyAmount();
        long finalMoney = userDTO.getMoney() + moneyIncome;
        userDTO.setMoney(finalMoney);

        /** 同步金钱数量（推送）**/
        pushPbService.moneySync(userId);

        // 解锁boss
        CfgBossDTO unclockCfgBossDTO = CfgManagerSingleton.getInstance().getCfgBossByPreBossIdFromCache(cfgBossDTO.getBossId());
        if(unclockCfgBossDTO != null){
            UserBossDTO unlockedUserBossDTO = UserManagerSingleton.getInstance().getUserBossByIdFromCache(userId,unclockCfgBossDTO.getBossId());
            unlockedUserBossDTO.setUnlocked(true);
            challengeBossSuccessResPb.setUnlockedBossId(unlockedUserBossDTO.getBossId());
        }

        // 检测载具（新）前置条件是否清除
        Map<Integer,CfgVehicleNewDTO> cfgVehicleNewDTOMap = CfgManagerSingleton.getInstance().getCfgVehicleNewDTOMap();
        for(Map.Entry<Integer,CfgVehicleNewDTO> entry : cfgVehicleNewDTOMap.entrySet()){
            CfgVehicleNewDTO cfgVehicleNewDTO = entry.getValue();
            // 挑战榜类型：0 富豪 1 BOSS
            if(cfgVehicleNewDTO.getPreConditionChallengeType() == 1 && cfgVehicleNewDTO.getPreConditionChallengeId() == arg.getBossId()){
                UserVehicleNewDTO userVehicleNewDTO = UserManagerSingleton.getInstance().getUserVehicleNewByIdFromCache(userId,cfgVehicleNewDTO.getVehicleId());
                if(userVehicleNewDTO == null) break;
                if(userVehicleNewDTO.isPreConditionClear()) break;
                if(userVehicleNewDTO.isUnlocked()) break;
                userVehicleNewDTO.setPreConditionClear(true);
                challengeBossSuccessResPb.setPreConditionClearVehicleNewId(cfgVehicleNewDTO.getVehicleId());
                break;
            }
        }

        log.info("ChallengeBossSuccessExecutor::executor:userId = {},challengeBossSuccessResPb = {},end",userId,challengeBossSuccessResPb);
        return challengeBossSuccessResPb;
    }
}

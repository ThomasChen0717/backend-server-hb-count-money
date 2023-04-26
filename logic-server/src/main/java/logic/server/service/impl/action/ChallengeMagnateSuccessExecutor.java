package logic.server.service.impl.action;

import common.pb.enums.ErrorCodeEnum;
import common.pb.pb.ChallengeMagnateSuccessReqPb;
import common.pb.pb.ChallengeMagnateSuccessResPb;
import logic.server.dto.CfgMagnateDTO;
import logic.server.dto.UserDTO;
import logic.server.dto.UserMagnateDTO;
import logic.server.dto.UserVehicleDTO;
import logic.server.service.IPushPbService;
import logic.server.singleton.CfgManagerSingleton;
import logic.server.singleton.UserManagerSingleton;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ChallengeMagnateSuccessExecutor implements BaseExecutor<ChallengeMagnateSuccessReqPb, ChallengeMagnateSuccessResPb, Long>{
    @Autowired
    private IPushPbService pushPbService;
    @Override
    public ChallengeMagnateSuccessResPb executor(ChallengeMagnateSuccessReqPb arg, Long userId){
        log.info("ChallengeMagnateSuccessExecutor::executor:userId = {},start",userId);

        UserDTO userDTO = UserManagerSingleton.getInstance().getUserByIdFromCache(userId);
        ErrorCodeEnum.userNotExist.assertNonNull(userDTO);
        UserMagnateDTO userMagnateDTO = UserManagerSingleton.getInstance().getUserMagnateByIdFromCache(userId,arg.getMagnateId());
        ErrorCodeEnum.magnateNotExist.assertNonNull(userMagnateDTO);
        ErrorCodeEnum.magnateIsLock.assertTrue(userMagnateDTO.isUnlocked());
        CfgMagnateDTO cfgMagnateDTO = CfgManagerSingleton.getInstance().getCfgMagnateByIdFromCache(arg.getMagnateId());

        long moneyIncome = arg.getMultiple() * cfgMagnateDTO.getRewardMoneyAmount();
        long finalMoney = userDTO.getMoney() + moneyIncome;
        userDTO.setMoney(finalMoney);

        /** 同步金钱数量（推送）**/
        pushPbService.moneySync(userId);

        ChallengeMagnateSuccessResPb challengeMagnateSuccessResPb = new ChallengeMagnateSuccessResPb();
        // 解锁载具
        if(cfgMagnateDTO.getUnlockVehicleId() > 0){
            // 新载具解锁并且使用
            UserVehicleDTO unlockedUserVehicleDTO = UserManagerSingleton.getInstance().getUserVehicleByIdFromCache(userId,cfgMagnateDTO.getUnlockVehicleId());
            // 如是已解锁正常流程来说，当前使用的载具比解锁的还要高级
            if(!unlockedUserVehicleDTO.isUnlocked()){
                unlockedUserVehicleDTO.setInUse(true).setUnlocked(true);
                challengeMagnateSuccessResPb.setUnlockedVehicleId(cfgMagnateDTO.getUnlockVehicleId());

                // 当前载具设置为未使用
                UserVehicleDTO userVehicleDTO = UserManagerSingleton.getInstance().getUserUsingVehicleByIdFromCache(userId);
                userVehicleDTO.setInUse(false);
            }
        }
        // 解锁下一个富豪
        CfgMagnateDTO nextCfgMagnateDTO = CfgManagerSingleton.getInstance().getNextCfgMagnateByIdFromCache(arg.getMagnateId());
        if(nextCfgMagnateDTO != null){
            UserMagnateDTO nextUserMagnateDTO = UserManagerSingleton.getInstance().getUserMagnateByIdFromCache(userId,nextCfgMagnateDTO.getMagnateId());
            nextUserMagnateDTO.setUnlocked(true);
            challengeMagnateSuccessResPb.setNextMagnateId(nextCfgMagnateDTO.getMagnateId());
        }

        log.info("ChallengeMagnateSuccessExecutor::executor:userId = {},challengeMagnateSuccessResPb = {},end",userId,challengeMagnateSuccessResPb);
        return challengeMagnateSuccessResPb;
    }
}
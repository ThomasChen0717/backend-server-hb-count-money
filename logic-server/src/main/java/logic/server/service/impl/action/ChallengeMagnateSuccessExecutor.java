package logic.server.service.impl.action;

import common.pb.enums.ErrorCodeEnum;
import common.pb.pb.ChallengeMagnateSuccessReqPb;
import common.pb.pb.ChallengeMagnateSuccessResPb;
import logic.server.dto.CfgMagnateDTO;
import logic.server.dto.CfgVehicleNewDTO;
import logic.server.dto.UserDTO;
import logic.server.dto.UserMagnateDTO;
import logic.server.dto.UserVehicleDTO;
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
public class ChallengeMagnateSuccessExecutor implements BaseExecutor<ChallengeMagnateSuccessReqPb, ChallengeMagnateSuccessResPb, Long>{
    @Autowired
    private IPushPbService pushPbService;
    @Override
    public ChallengeMagnateSuccessResPb executor(ChallengeMagnateSuccessReqPb arg, Long userId){
        log.info("ChallengeMagnateSuccessExecutor::executor:userId = {},arg = {},start",userId,arg);
        ChallengeMagnateSuccessResPb challengeMagnateSuccessResPb = new ChallengeMagnateSuccessResPb();

        UserDTO userDTO = UserManagerSingleton.getInstance().getUserByIdFromCache(userId);
        UserMagnateDTO userMagnateDTO = UserManagerSingleton.getInstance().getUserMagnateByIdFromCache(userId,arg.getMagnateId());

        if(userMagnateDTO == null){
            challengeMagnateSuccessResPb.setCode(ErrorCodeEnum.magnateNotExist.getCode()).setMessage(ErrorCodeEnum.magnateNotExist.getMsg());
            log.info("ChallengeBossSuccessExecutor::executor:userId = {},challengeMagnateSuccessResPb = {},end",userId,challengeMagnateSuccessResPb);
            return challengeMagnateSuccessResPb;
        }
        if(!userMagnateDTO.isUnlocked()){
            challengeMagnateSuccessResPb.setCode(ErrorCodeEnum.magnateIsLock.getCode()).setMessage(ErrorCodeEnum.magnateIsLock.getMsg());
            log.info("ChallengeBossSuccessExecutor::executor:userId = {},challengeMagnateSuccessResPb = {},end",userId,challengeMagnateSuccessResPb);
            return challengeMagnateSuccessResPb;
        }

        CfgMagnateDTO cfgMagnateDTO = CfgManagerSingleton.getInstance().getCfgMagnateByIdFromCache(arg.getMagnateId());
        long moneyIncome = ((long)arg.getMultiple()) * ((long)cfgMagnateDTO.getRewardMoneyAmount());
        long finalMoney = userDTO.getMoney() + moneyIncome;
        userDTO.setMoney(finalMoney);

        /** 同步金钱数量（推送）**/
        pushPbService.moneySync(userId);

        userMagnateDTO.setBeat(true);
        UserMagnateDTO latestUserMagnateDTO = UserManagerSingleton.getInstance().getUserLatestBeatMagnateFromCache(userId);
        userDTO.setTitle(CfgManagerSingleton.getInstance().getCfgMagnateByIdFromCache(latestUserMagnateDTO.getMagnateId()).getMagnateName());
        /** 同步称号数量（推送）**/
        pushPbService.titleSync(userId);

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
        // 解锁富豪
        CfgMagnateDTO unlockCfgMagnateDTO = CfgManagerSingleton.getInstance().getCfgMagnateByPreMagnateIdFromCache(cfgMagnateDTO.getMagnateId());
        if(unlockCfgMagnateDTO != null){
            UserMagnateDTO nextUserMagnateDTO = UserManagerSingleton.getInstance().getUserMagnateByIdFromCache(userId,unlockCfgMagnateDTO.getMagnateId());
            nextUserMagnateDTO.setUnlocked(true);
            challengeMagnateSuccessResPb.setUnlockedMagnateId(unlockCfgMagnateDTO.getMagnateId());
        }

        challengeMagnateSuccessResPb.setBeatMagnateId(arg.getMagnateId());

        log.info("ChallengeMagnateSuccessExecutor::executor:userId = {},challengeMagnateSuccessResPb = {},end",userId,challengeMagnateSuccessResPb);
        return challengeMagnateSuccessResPb;
    }
}

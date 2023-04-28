package logic.server.service.impl.action;

import common.pb.enums.ErrorCodeEnum;
import common.pb.pb.UnlockVehicleOrEquipmentReqPb;
import common.pb.pb.UnlockVehicleOrEquipmentResPb;
import logic.server.dto.CfgEquipmentDTO;
import logic.server.dto.CfgVehicleDTO;
import logic.server.dto.UserDTO;
import logic.server.dto.UserEquipmentDTO;
import logic.server.dto.UserVehicleDTO;
import logic.server.enums.ConditionEnum;
import logic.server.service.IPushPbService;
import logic.server.singleton.CfgManagerSingleton;
import logic.server.singleton.UserManagerSingleton;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UnlockVehicleOrEquipmentExecutor implements BaseExecutor<UnlockVehicleOrEquipmentReqPb, UnlockVehicleOrEquipmentResPb,Long>{
    @Autowired
    private IPushPbService pushPbService;
    @Override
    public UnlockVehicleOrEquipmentResPb executor(UnlockVehicleOrEquipmentReqPb arg, Long userId){
        log.info("UnlockVehicleOrEquipmentExecutor::executor:userId = {},arg = {},start",userId,arg);
        UnlockVehicleOrEquipmentResPb unlockVehicleOrEquipmentResPb = new UnlockVehicleOrEquipmentResPb();

        UserDTO userDTO = UserManagerSingleton.getInstance().getUserByIdFromCache(userId);
        // 解锁载具
        if(arg.getType() == 1){
            UserVehicleDTO userVehicleDTO = UserManagerSingleton.getInstance().getUserVehicleByIdFromCache(userId,arg.getItemId());
            if(userVehicleDTO == null){
                unlockVehicleOrEquipmentResPb.setCode(ErrorCodeEnum.vehicleNotExist.getCode()).setMessage(ErrorCodeEnum.vehicleNotExist.getMsg());
                log.info("UnlockVehicleOrEquipmentExecutor::executor:userId = {},unlockVehicleOrEquipmentResPb = {},end",userId,unlockVehicleOrEquipmentResPb);
                return unlockVehicleOrEquipmentResPb;
            }
            boolean isUnlocked = false;
            CfgVehicleDTO cfgVehicleDTO = CfgManagerSingleton.getInstance().getCfgVehicleByIdFromCache(userVehicleDTO.getVehicleId());
            if(cfgVehicleDTO.getUnlockConditionType() == ConditionEnum.ad.getConditionType()){
                // 解锁条件是广告类型
                userVehicleDTO.setUnlockConditionCurrCount(userVehicleDTO.getUnlockConditionCurrCount() + 1);
                if(userVehicleDTO.getUnlockConditionCurrCount() >= cfgVehicleDTO.getUnlockConditionCount()){
                    isUnlocked = true;
                }
            }else if(cfgVehicleDTO.getUnlockConditionType() == ConditionEnum.money.getConditionType()){
                // 解锁条件是金钱类型
                long leftMoney = userDTO.getMoney() - cfgVehicleDTO.getUnlockConditionCount();
                if(leftMoney > 0){
                    isUnlocked = true;
                    userDTO.setMoney(leftMoney);
                    /** 同步金钱数量（推送）**/
                    pushPbService.moneySync(userId);
                }else{
                    unlockVehicleOrEquipmentResPb.setCode(ErrorCodeEnum.moneyCostNotEnough.getCode()).setMessage(ErrorCodeEnum.moneyCostNotEnough.getMsg());
                    log.info("UnlockVehicleOrEquipmentExecutor::executor:userId = {},unlockVehicleOrEquipmentResPb = {},end",userId,unlockVehicleOrEquipmentResPb);
                    return unlockVehicleOrEquipmentResPb;
                }
            }
            userVehicleDTO.setUnlocked(isUnlocked);
            userVehicleDTO.setInUse(true);
            UserVehicleDTO usingUserVehicleDTO = UserManagerSingleton.getInstance().getUserUsingVehicleByIdFromCache(userId);
            if(usingUserVehicleDTO != null) usingUserVehicleDTO.setInUse(false);
        }else if(arg.getType() == 2){
            // 解锁装备
            UserEquipmentDTO userEquipmentDTO = UserManagerSingleton.getInstance().getUserEquipmentByIdFromCache(userId,arg.getItemId());
            if(userEquipmentDTO == null){
                unlockVehicleOrEquipmentResPb.setCode(ErrorCodeEnum.equipmentNotExist.getCode()).setMessage(ErrorCodeEnum.equipmentNotExist.getMsg());
                log.info("UnlockVehicleOrEquipmentExecutor::executor:userId = {},unlockVehicleOrEquipmentResPb = {},end",userId,unlockVehicleOrEquipmentResPb);
                return unlockVehicleOrEquipmentResPb;
            }
            CfgEquipmentDTO cfgEquipmentDTO = CfgManagerSingleton.getInstance().getCfgEquipmentByIdFromCache(userEquipmentDTO.getEquipmentId());
            if(cfgEquipmentDTO.getUnlockConditionType() == ConditionEnum.ad.getConditionType()){
                // 解锁条件是广告类型
                userEquipmentDTO.setUnlockConditionCurrCount(userEquipmentDTO.getUnlockConditionCurrCount() + 1);
                if(userEquipmentDTO.getUnlockConditionCurrCount() >= cfgEquipmentDTO.getUnlockConditionCount()){
                    userEquipmentDTO.setUnlocked(true);
                }
            }else if(cfgEquipmentDTO.getUnlockConditionType() == ConditionEnum.money.getConditionType()){
                // 解锁条件是金钱类型
                long leftMoney = userDTO.getMoney() - cfgEquipmentDTO.getUnlockConditionCount();
                if(leftMoney > 0){
                    userEquipmentDTO.setUnlocked(true);
                    userDTO.setMoney(leftMoney);
                    /** 同步金钱数量（推送）**/
                    pushPbService.moneySync(userId);
                }else{
                    unlockVehicleOrEquipmentResPb.setCode(ErrorCodeEnum.moneyCostNotEnough.getCode()).setMessage(ErrorCodeEnum.moneyCostNotEnough.getMsg());
                    log.info("UnlockVehicleOrEquipmentExecutor::executor:userId = {},unlockVehicleOrEquipmentResPb = {},end",userId,unlockVehicleOrEquipmentResPb);
                    return unlockVehicleOrEquipmentResPb;
                }
            }
        }else{
            unlockVehicleOrEquipmentResPb.setCode(ErrorCodeEnum.unlockTypeError.getCode()).setMessage(ErrorCodeEnum.unlockTypeError.getMsg());
            log.info("UnlockVehicleOrEquipmentExecutor::executor:userId = {},unlockVehicleOrEquipmentResPb = {},end",userId,unlockVehicleOrEquipmentResPb);
            return unlockVehicleOrEquipmentResPb;
        }

        log.info("UnlockVehicleOrEquipmentExecutor::executor:userId = {},unlockVehicleOrEquipmentResPb = {},end",userId, unlockVehicleOrEquipmentResPb);
        return unlockVehicleOrEquipmentResPb;
    }
}

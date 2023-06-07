package logic.server.service.impl.action;

import com.iohao.game.action.skeleton.core.exception.MsgException;
import common.pb.enums.ErrorCodeEnum;
import common.pb.pb.VehicleNewLevelUpReqPb;
import common.pb.pb.VehicleNewLevelUpResPb;
import logic.server.dto.UserAttributeDTO;
import logic.server.dto.UserDTO;
import logic.server.dto.UserVehicleNewDTO;
import logic.server.service.IPushPbService;
import logic.server.singleton.UserManagerSingleton;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class VehicleNewLevelUpExecutor implements BaseExecutor<VehicleNewLevelUpReqPb, VehicleNewLevelUpResPb,Long>{
    @Autowired
    private IPushPbService pushPbService;
    @Override
    public VehicleNewLevelUpResPb executor(VehicleNewLevelUpReqPb arg, Long userId) throws MsgException {
        log.info("VehicleNewLevelUpExecutor::executor:userId = {},arg = {},start",userId,arg);
        VehicleNewLevelUpResPb vehicleNewLevelUpResPb = new VehicleNewLevelUpResPb();

        if(arg.getMoneyCost() < 0){
            vehicleNewLevelUpResPb.setCode(ErrorCodeEnum.levelUpMoneyCostError.getCode()).setMessage(ErrorCodeEnum.levelUpMoneyCostError.getMsg());
            log.info("VehicleNewLevelUpExecutor::executor:userId = {},vehicleNewLevelUpResPb = {},end",userId,vehicleNewLevelUpResPb);
            return vehicleNewLevelUpResPb;
        }

        UserDTO userDTO = UserManagerSingleton.getInstance().getUserByIdFromCache(userId);
        if(userDTO.getMoney() < arg.getMoneyCost()){
            vehicleNewLevelUpResPb.setCode(ErrorCodeEnum.moneyCostNotEnough.getCode()).setMessage(ErrorCodeEnum.moneyCostNotEnough.getMsg());
            log.info("VehicleNewLevelUpExecutor::executor:userId = {},vehicleNewLevelUpResPb = {},end",userId,vehicleNewLevelUpResPb);
            return vehicleNewLevelUpResPb;
        }

        UserVehicleNewDTO userVehicleNewDTO = UserManagerSingleton.getInstance().getUserVehicleNewByIdFromCache(userId,arg.getVehicleId());
        if(userVehicleNewDTO != null && userVehicleNewDTO.isUnlocked()){
            userVehicleNewDTO.setLevel(arg.getTargetLevel());
            vehicleNewLevelUpResPb.setVehicleId(arg.getVehicleId());
            vehicleNewLevelUpResPb.setLevel(arg.getTargetLevel());

            long finalMoney = userDTO.getMoney() - arg.getMoneyCost();
            userDTO.setMoney(finalMoney);
            /** 同步金钱数量（推送）**/
            pushPbService.moneySync(userId);
        }

        log.info("VehicleNewLevelUpExecutor::executor:userId = {},vehicleNewLevelUpResPb = {},end",userId,vehicleNewLevelUpResPb);
        return vehicleNewLevelUpResPb;
    }
}

package logic.server.service.impl.action;

import common.pb.enums.ErrorCodeEnum;
import common.pb.pb.ChangeVehicleReqPb;
import common.pb.pb.ChangeVehicleResPb;
import logic.server.dto.UserVehicleDTO;
import logic.server.singleton.UserManagerSingleton;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ChangeVehicleExecutor implements BaseExecutor<ChangeVehicleReqPb, ChangeVehicleResPb,Long>{
    @Override
    public ChangeVehicleResPb executor(ChangeVehicleReqPb arg, Long userId){
        log.info("ChangeVehicleExecutor::executor:userId = {},arg = {},start",userId,arg);
        ChangeVehicleResPb changeVehicleResPb = new ChangeVehicleResPb();

        UserVehicleDTO userVehicleDTO = UserManagerSingleton.getInstance().getUserUsingVehicleByIdFromCache(userId);
        if(userVehicleDTO.getVehicleId() == arg.getTargetVehicleId()){
            changeVehicleResPb.setCode(ErrorCodeEnum.vehicleIsUsing.getCode()).setMessage(ErrorCodeEnum.vehicleIsUsing.getMsg());
            log.info("ChangeVehicleExecutor::executor:userId = {},changeVehicleResPb = {},end",userId,changeVehicleResPb);
            return changeVehicleResPb;
        }
        UserVehicleDTO userTargetVehicleDTO = UserManagerSingleton.getInstance().getUserVehicleByIdFromCache(userId,arg.getTargetVehicleId());
        if(userTargetVehicleDTO == null){
            changeVehicleResPb.setCode(ErrorCodeEnum.vehicleNotExist.getCode()).setMessage(ErrorCodeEnum.vehicleNotExist.getMsg());
            log.info("ChangeVehicleExecutor::executor:userId = {},changeVehicleResPb = {},end",userId,changeVehicleResPb);
            return changeVehicleResPb;
        }
        if(!userTargetVehicleDTO.isUnlocked()){
            changeVehicleResPb.setCode(ErrorCodeEnum.vehicleIsLock.getCode()).setMessage(ErrorCodeEnum.vehicleIsLock.getMsg());
            log.info("ChangeVehicleExecutor::executor:userId = {},changeVehicleResPb = {},end",userId,changeVehicleResPb);
            return changeVehicleResPb;
        }

        userVehicleDTO.setInUse(false);
        userTargetVehicleDTO.setInUse(true);

        changeVehicleResPb.setVehicleId(arg.getTargetVehicleId()).setUnloadVehicleId(userVehicleDTO.getVehicleId());
        log.info("ChangeVehicleExecutor::executor:userId = {},changeVehicleResPb = {},end",userId,changeVehicleResPb);
        return changeVehicleResPb;
    }
}

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
        log.info("ChangeVehicleExecutor::executor:userId = {},start",userId);

        UserVehicleDTO userVehicleDTO = UserManagerSingleton.getInstance().getUserUsingVehicleByIdFromCache(userId);
        ErrorCodeEnum.vehicleIsUsing.assertTrue(!(userVehicleDTO.getVehicleId() == arg.getTargetVehicleId()) );
        UserVehicleDTO userTargetVehicleDTO = UserManagerSingleton.getInstance().getUserVehicleByIdFromCache(userId,arg.getTargetVehicleId());
        ErrorCodeEnum.vehicleNotExist.assertNonNull(userTargetVehicleDTO == null);
        ErrorCodeEnum.vehicleIsLock.assertTrue(userTargetVehicleDTO.isUnlocked());

        userVehicleDTO.setInUse(false);
        userTargetVehicleDTO.setInUse(true);

        ChangeVehicleResPb changeVehicleResPb = new ChangeVehicleResPb();
        changeVehicleResPb.setVehicleId(arg.getTargetVehicleId()).setUnloadVehicleId(userVehicleDTO.getVehicleId());
        log.info("ChangeVehicleExecutor::executor:userId = {},changeVehicleResPb = {},end",userId,changeVehicleResPb);
        return changeVehicleResPb;
    }
}

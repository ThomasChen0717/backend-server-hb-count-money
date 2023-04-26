package logic.server.service.impl.action;

import common.pb.enums.ErrorCodeEnum;
import common.pb.pb.UseEquipmentReqPb;
import common.pb.pb.UseEquipmentResPb;
import logic.server.dto.CfgEquipmentDTO;
import logic.server.dto.UserEquipmentDTO;
import logic.server.singleton.CfgManagerSingleton;
import logic.server.singleton.UserManagerSingleton;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class UseEquipmentExecutor implements BaseExecutor<UseEquipmentReqPb, UseEquipmentResPb,Long>{

    @Override
    public UseEquipmentResPb executor(UseEquipmentReqPb arg, Long userId){
        log.info("UseEquipmentExecutor::executor:userId = {},arg = {},start",userId,arg);
        UseEquipmentResPb useEquipmentResPb = new UseEquipmentResPb();

        UserEquipmentDTO targetUserEquipmentDTO = UserManagerSingleton.getInstance().getUserEquipmentByIdFromCache(userId,arg.getTargetEquipmentId());
        if(targetUserEquipmentDTO == null){
            useEquipmentResPb.setCode(ErrorCodeEnum.equipmentNotExist.getCode()).setMessage(ErrorCodeEnum.equipmentNotExist.getMsg());
            log.info("UseEquipmentExecutor::executor:userId = {},useEquipmentResPb = {},end",userId,useEquipmentResPb);
            return useEquipmentResPb;
        }
        if(targetUserEquipmentDTO.isInUse()){
            useEquipmentResPb.setCode(ErrorCodeEnum.equipmentIsUsing.getCode()).setMessage(ErrorCodeEnum.equipmentIsUsing.getMsg());
            log.info("UseEquipmentExecutor::executor:userId = {},useEquipmentResPb = {},end",userId,useEquipmentResPb);
            return useEquipmentResPb;
        }
        if(!targetUserEquipmentDTO.isUnlocked()){
            useEquipmentResPb.setCode(ErrorCodeEnum.equipmentIsLock.getCode()).setMessage(ErrorCodeEnum.equipmentIsLock.getMsg());
            log.info("UseEquipmentExecutor::executor:userId = {},useEquipmentResPb = {},end",userId,useEquipmentResPb);
            return useEquipmentResPb;
        }

        // 检测是否已经使用了同种效果的装备
        UserEquipmentDTO sameAttributeTypeUserEquipmentDTO = null;
        CfgEquipmentDTO targetCfgEquipmentDTO = CfgManagerSingleton.getInstance().getCfgEquipmentByIdFromCache(arg.getTargetEquipmentId());
        List<UserEquipmentDTO> userEquipmentDTOList = UserManagerSingleton.getInstance().getUserEquipmentListByAttributeTypeFromCache(userId,targetCfgEquipmentDTO.getEffectAttributeType());
        for(UserEquipmentDTO userEquipmentDTO : userEquipmentDTOList){
            if(userEquipmentDTO.isInUse() && userEquipmentDTO.getEquipmentId() != targetUserEquipmentDTO.getEquipmentId()){
                sameAttributeTypeUserEquipmentDTO = userEquipmentDTO;
                break;
            }
        }
        if(sameAttributeTypeUserEquipmentDTO != null) sameAttributeTypeUserEquipmentDTO.setInUse(false);
        targetUserEquipmentDTO.setInUse(true);

        useEquipmentResPb.setEquipmentId(arg.getTargetEquipmentId()).setUnloadEquipmentId(sameAttributeTypeUserEquipmentDTO == null ? 0 : sameAttributeTypeUserEquipmentDTO.getEquipmentId());
        log.info("UseEquipmentExecutor::executor:userId = {},useEquipmentResPb = {},end",userId,useEquipmentResPb);
        return useEquipmentResPb;
    }
}

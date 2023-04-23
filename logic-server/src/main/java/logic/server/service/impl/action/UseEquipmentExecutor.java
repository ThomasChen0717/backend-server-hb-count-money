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
        log.info("UseEquipmentExecutor::executor:userId = {},start",userId);

        UserEquipmentDTO targetUserEquipmentDTO = UserManagerSingleton.getInstance().getUserEquipmentByIdFromCache(userId,arg.getTargetEquipmentId());
        ErrorCodeEnum.equipmentNotExist.assertNonNull(targetUserEquipmentDTO);
        ErrorCodeEnum.equipmentIsUsing.assertTrue(!targetUserEquipmentDTO.isInUse());

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

        UseEquipmentResPb useEquipmentResPb = new UseEquipmentResPb();
        useEquipmentResPb.setEquipmentId(arg.getTargetEquipmentId()).setUnloadEquipmentId(sameAttributeTypeUserEquipmentDTO == null ? 0 : sameAttributeTypeUserEquipmentDTO.getEquipmentId());
        log.info("UseEquipmentExecutor::executor:userId = {},useEquipmentResPb = {},end",userId,useEquipmentResPb);
        return useEquipmentResPb;
    }
}

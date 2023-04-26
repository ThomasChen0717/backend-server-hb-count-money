package logic.server.service.impl.action;

import com.iohao.game.action.skeleton.core.exception.MsgException;
import common.pb.enums.ErrorCodeEnum;
import common.pb.pb.AttributeLevelUpReqPb;
import common.pb.pb.AttributeLevelUpResPb;
import logic.server.dto.UserAttributeDTO;
import logic.server.dto.UserDTO;
import logic.server.enums.AttributeEnum;
import logic.server.service.IPushPbService;
import logic.server.singleton.UserManagerSingleton;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AttributeLevelUpExecutor implements BaseExecutor<AttributeLevelUpReqPb, AttributeLevelUpResPb,Long>{
    @Autowired
    private IPushPbService pushPbService;

    @Override
    public AttributeLevelUpResPb executor(AttributeLevelUpReqPb arg, Long userId) throws MsgException {
        log.info("AttributeLevelUpExecutor::executor:userId = {},arg = {},start",userId,arg);
        AttributeLevelUpResPb attributeLevelUpResPb = new AttributeLevelUpResPb();

        if(arg.getMoneyCost() < 0){
            attributeLevelUpResPb.setCode(ErrorCodeEnum.levelUpMoneyCostError.getCode()).setMessage(ErrorCodeEnum.levelUpMoneyCostError.getMsg());
            log.info("AttributeLevelUpExecutor::executor:userId = {},attributeLevelUpResPb = {},end",userId,attributeLevelUpResPb);
            return attributeLevelUpResPb;
        }

        UserDTO userDTO = UserManagerSingleton.getInstance().getUserByIdFromCache(userId);
        UserAttributeDTO userAttributeDTO = UserManagerSingleton.getInstance().getUserAttributeFromCache(userId);
        if(userDTO.getMoney() < arg.getMoneyCost()){
            attributeLevelUpResPb.setCode(ErrorCodeEnum.levelUpMoneyCostNotEnough.getCode()).setMessage(ErrorCodeEnum.levelUpMoneyCostNotEnough.getMsg());
            log.info("AttributeLevelUpExecutor::executor:userId = {},attributeLevelUpResPb = {},end",userId,attributeLevelUpResPb);
            return attributeLevelUpResPb;
        }

        int attributeLevel = 0;
        if(arg.getAttributeType() == AttributeEnum.strengthLevel.getAttributeType()){
            // 力量等级
            attributeLevel = userAttributeDTO.getStrengthLevel();
        }else if(arg.getAttributeType() == AttributeEnum.physicalLevel.getAttributeType()){
            // 体力等级
            attributeLevel = userAttributeDTO.getPhysicalLevel();
        }else if(arg.getAttributeType() == AttributeEnum.physicalRestoreLevel.getAttributeType()){
            // 体力恢复等级
            attributeLevel = userAttributeDTO.getPhysicalRestoreLevel();
        }else if(arg.getAttributeType() == AttributeEnum.enduranceLevel.getAttributeType()){
            // 耐力等级
            attributeLevel = userAttributeDTO.getEnduranceLevel();
        }else if(arg.getAttributeType() == AttributeEnum.petLevel.getAttributeType()){
            // 宠物等级
            attributeLevel = userAttributeDTO.getPetLevel();
        }
        ErrorCodeEnum.attributeTypeError.assertTrue(attributeLevel > 0);
        ErrorCodeEnum.targetLevelError.assertTrue(arg.getTargetLevel() > attributeLevel);
        if(arg.getAttributeType() == AttributeEnum.strengthLevel.getAttributeType()){
            // 力量等级
            userAttributeDTO.setStrengthLevel(arg.getTargetLevel());
        }else if(arg.getAttributeType() == AttributeEnum.physicalLevel.getAttributeType()){
            // 体力等级
            userAttributeDTO.setPhysicalLevel(arg.getTargetLevel());
        }else if(arg.getAttributeType() == AttributeEnum.physicalRestoreLevel.getAttributeType()){
            // 体力恢复等级
            userAttributeDTO.setPhysicalRestoreLevel(arg.getTargetLevel());
        }else if(arg.getAttributeType() == AttributeEnum.enduranceLevel.getAttributeType()){
            // 耐力等级
            userAttributeDTO.setEnduranceLevel(arg.getTargetLevel());
        }else if(arg.getAttributeType() == AttributeEnum.petLevel.getAttributeType()){
            // 宠物等级
            userAttributeDTO.setPetLevel(arg.getTargetLevel());
        }

        long finalMoney = userDTO.getMoney() - arg.getMoneyCost();
        userDTO.setMoney(finalMoney);
        /** 同步金钱数量（推送）**/
        pushPbService.moneySync(userId);

        attributeLevelUpResPb.setAttributeType(arg.getAttributeType()).setLevel(arg.getTargetLevel());
        log.info("AttributeLevelUpExecutor::executor:userId = {},attributeLevelUpResPb = {},end",userId,attributeLevelUpResPb);
        return attributeLevelUpResPb;
    }
}

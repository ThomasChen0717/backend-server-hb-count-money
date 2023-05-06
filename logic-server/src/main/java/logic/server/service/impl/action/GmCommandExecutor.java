package logic.server.service.impl.action;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import common.pb.enums.ErrorCodeEnum;
import common.pb.pb.GmCommandReqPb;
import common.pb.pb.GmCommandResPb;
import logic.server.dto.UserAttributeDTO;
import logic.server.dto.UserBossDTO;
import logic.server.dto.UserDTO;
import logic.server.dto.UserEquipmentDTO;
import logic.server.dto.UserMagnateDTO;
import logic.server.dto.UserVehicleDTO;
import logic.server.enums.AttributeEnum;
import logic.server.enums.GmCommandEnum;
import logic.server.service.IPushPbService;
import logic.server.singleton.UserManagerSingleton;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
public class GmCommandExecutor implements BaseExecutor<GmCommandReqPb, GmCommandResPb,Long> {
    @Autowired
    private IPushPbService pushPbService;
    @Override
    public GmCommandResPb executor(GmCommandReqPb arg, Long userId){
        log.info("GmCommandExecutor::executor:userId = {},arg = {},start",userId,arg);
        GmCommandResPb gmCommandResPb = new GmCommandResPb();

        // 检测用户权限
        UserDTO userDTO = UserManagerSingleton.getInstance().getUserByIdFromCache(userId);
        if(userDTO == null){
            gmCommandResPb.setCode(ErrorCodeEnum.userNotExist.getCode()).setMessage(ErrorCodeEnum.userNotExist.getMsg());
            log.info("GmCommandExecutor::executor:userId = {},gmCommandResPb = {},end",userId,gmCommandResPb);
            return gmCommandResPb;
        }
        if(userDTO.getPrivilegeLevel() < 1){
            gmCommandResPb.setCode(ErrorCodeEnum.permissionDenied.getCode()).setMessage(ErrorCodeEnum.permissionDenied.getMsg());
            log.info("GmCommandExecutor::executor:userId = {},gmCommandResPb = {},end",userId,gmCommandResPb);
            return gmCommandResPb;
        }

        JSONObject jsonGmCommandInfo = JSON.parseObject(arg.getJsonGmCommandInfo());
        if(arg.getGmCommandId() == GmCommandEnum.addMoney.getGmCommandId()){
            /** 加钱命令:
             * {
             *      "money":10
             * }
             **/
            long money = jsonGmCommandInfo.getLongValue("money");
            if(money < 0){
                gmCommandResPb.setCode(ErrorCodeEnum.paramError.getCode()).setMessage(ErrorCodeEnum.paramError.getMsg());
                log.info("GmCommandExecutor::executor:userId = {},gmCommandResPb = {},end",userId,gmCommandResPb);
                return gmCommandResPb;
            }
            long finalMoney = userDTO.getMoney() + money;
            userDTO.setMoney(finalMoney);

            /** 同步金钱数量（推送）**/
            pushPbService.moneySync(userId);
        }else if(arg.getGmCommandId() == GmCommandEnum.unlockEquipment.getGmCommandId()){
            /** 装备全解锁命令:
             * {}
             **/
            Map<Integer,UserEquipmentDTO> userEquipmentDTOMap = UserManagerSingleton.getInstance().getUserEquipmentMapByIdFromCache(userId);
            for(Map.Entry<Integer,UserEquipmentDTO> entry : userEquipmentDTOMap.entrySet()){
                entry.getValue().setUnlocked(true);
            }
        }else if(arg.getGmCommandId() == GmCommandEnum.unlockVehicle.getGmCommandId()){
            /** 载具全解锁命令:
             * {}
             **/
            Map<Integer, UserVehicleDTO> userVehicleDTOMap = UserManagerSingleton.getInstance().getUserVehicleMapByIdFromCache(userId);
            for(Map.Entry<Integer,UserVehicleDTO> entry : userVehicleDTOMap.entrySet()){
                entry.getValue().setUnlocked(true);
            }
        }else if(arg.getGmCommandId() == GmCommandEnum.unlockMagnate.getGmCommandId()){
            /** 富豪全解锁命令:
             * {}
             **/
            Map<Integer, UserMagnateDTO> userMagnateDTOMap = UserManagerSingleton.getInstance().getUserMagnateMapByIdFromCache(userId);
            for(Map.Entry<Integer,UserMagnateDTO> entry : userMagnateDTOMap.entrySet()){
                entry.getValue().setUnlocked(true);
            }
        }else if(arg.getGmCommandId() == GmCommandEnum.unlockBoss.getGmCommandId()){
            /** boss全解锁命令:
             * {}
             **/
            Map<Integer, UserBossDTO> userBossDTOMap = UserManagerSingleton.getInstance().getUserBossMapByIdFromCache(userId);
            for(Map.Entry<Integer,UserBossDTO> entry : userBossDTOMap.entrySet()){
                entry.getValue().setUnlocked(true);
            }
        }else if(arg.getGmCommandId() == GmCommandEnum.attributeLevelUp.getGmCommandId()){
            /** 属性升级命令:
             * {
             *     "attributeType":1,
             *     "targetLevel":1000
             * }
             **/
            int attributeType = jsonGmCommandInfo.getIntValue("attributeType");
            int targetLevel = jsonGmCommandInfo.getIntValue("targetLevel");
            if(targetLevel <= 0 || targetLevel > 100000){
                gmCommandResPb.setCode(ErrorCodeEnum.paramError.getCode()).setMessage(ErrorCodeEnum.paramError.getMsg());
                log.info("GmCommandExecutor::executor:userId = {},gmCommandResPb = {},end",userId,gmCommandResPb);
                return gmCommandResPb;
            }
            UserAttributeDTO userAttributeDTO = UserManagerSingleton.getInstance().getUserAttributeFromCache(userId);
            if(attributeType == AttributeEnum.strengthLevel.getAttributeType()){
                // 力量等级
                userAttributeDTO.setStrengthLevel(targetLevel);
            }else if(attributeType == AttributeEnum.physicalLevel.getAttributeType()){
                // 体力等级
                userAttributeDTO.setPhysicalLevel(targetLevel);
            }else if(attributeType == AttributeEnum.physicalRestoreLevel.getAttributeType()){
                // 体力恢复等级
                userAttributeDTO.setPhysicalRestoreLevel(targetLevel);
            }else if(attributeType == AttributeEnum.enduranceLevel.getAttributeType()){
                // 耐力等级
                userAttributeDTO.setEnduranceLevel(targetLevel);
            }else if(attributeType == AttributeEnum.petLevel.getAttributeType()){
                // 宠物等级
                userAttributeDTO.setPetLevel(targetLevel);
            }
        }

        log.info("GmCommandExecutor::executor:userId = {},gmCommandResPb = {},end",userId,gmCommandResPb);
        return gmCommandResPb;
    }
}

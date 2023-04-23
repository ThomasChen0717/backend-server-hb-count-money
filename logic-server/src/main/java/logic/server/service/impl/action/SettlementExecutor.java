package logic.server.service.impl.action;

import common.pb.pb.SettlementReqPb;
import common.pb.pb.SettlementResPb;
import logic.server.dto.CfgVehicleDTO;
import logic.server.dto.UserAttributeDTO;
import logic.server.dto.UserDTO;
import logic.server.dto.UserVehicleDTO;
import logic.server.enums.RoleEnum;
import logic.server.service.IPushPbService;
import logic.server.singleton.CfgManagerSingleton;
import logic.server.singleton.UserManagerSingleton;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
@Slf4j
@Service
public class SettlementExecutor implements BaseExecutor<SettlementReqPb,SettlementResPb,Long>{
    @Autowired
    private IPushPbService pushPbService;

    @Override
    public SettlementResPb executor(SettlementReqPb arg,Long userId){
        log.info("SettlementExecutor::executor:userId = {},start",userId);
        UserDTO userDTO = UserManagerSingleton.getInstance().getUserByIdFromCache(userId);
        UserAttributeDTO userAttributeDTO = UserManagerSingleton.getInstance().getUserAttributeFromCache(userId);
        long moneyIncome = 0;
        if(arg.getSettlementRole() == RoleEnum.user.getRoleType()){
            // 主角结算-载具容量满
            // 最终收益 = 角色收益倍数属性 * （载具容量 + 载具额外奖励数值）
            UserVehicleDTO userVehicleDTO = UserManagerSingleton.getInstance().getUserUsingVehicleByIdFromCache(userId);
            CfgVehicleDTO cfgVehicleDTO = CfgManagerSingleton.getInstance().getCfgVehicleByTypeFromCache(userVehicleDTO.getVehicleId());
            moneyIncome = (long)(UserManagerSingleton.getInstance().getUserIncomeMultipleAttributeFromCache(userId) * (cfgVehicleDTO.getVehicleCapacity() + cfgVehicleDTO.getExtraRewardValue()));
        }else if(arg.getSettlementRole() == RoleEnum.pet.getRoleType()){
            // 宠物结算
            // 最终收益 = 宠物等级对应的收益
            moneyIncome = userAttributeDTO.getPetLevel() + 1;
        }
        long finalMoney = userDTO.getMoney() + moneyIncome;
        long finalMoneyHistory = userDTO.getMoneyHistory() + moneyIncome;
        userDTO.setMoney(finalMoney);
        userDTO.setMoneyHistory(finalMoneyHistory);

        /** 同步金钱数量（推送）**/
        pushPbService.moneySync(userId);

        SettlementResPb settlementResPb = new SettlementResPb();
        log.info("SettlementExecutor::executor:userId = {},settlementResPb = {},end",userId,settlementResPb);
        return settlementResPb;
    }
}

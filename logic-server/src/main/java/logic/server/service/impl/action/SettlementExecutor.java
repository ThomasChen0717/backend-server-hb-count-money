package logic.server.service.impl.action;

import com.alibaba.fastjson.JSONObject;
import common.pb.pb.SettlementReqPb;
import common.pb.pb.SettlementResPb;
import logic.server.dto.CfgVehicleDTO;
import logic.server.dto.CfgVipDTO;
import logic.server.dto.UserAttributeDTO;
import logic.server.dto.UserDTO;
import logic.server.dto.UserVehicleDTO;
import logic.server.dto.UserVipDTO;
import logic.server.enums.AttributeEnum;
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
        log.info("SettlementExecutor::executor:userId = {},arg = {},start",userId,arg);
        SettlementResPb settlementResPb = new SettlementResPb();

        UserDTO userDTO = UserManagerSingleton.getInstance().getUserByIdFromCache(userId);
        UserAttributeDTO userAttributeDTO = UserManagerSingleton.getInstance().getUserAttributeFromCache(userId);
        long moneyIncome = 0;
        if(arg.getSettlementRole() == RoleEnum.user.getRoleType()){
            // 主角结算-载具容量满
            // 最终收益 = 角色收益倍数属性 * （载具额外奖励数值）
            UserVehicleDTO userVehicleDTO = UserManagerSingleton.getInstance().getUserUsingVehicleByIdFromCache(userId);
            CfgVehicleDTO cfgVehicleDTO = CfgManagerSingleton.getInstance().getCfgVehicleByIdFromCache(userVehicleDTO.getVehicleId());
            moneyIncome = (long)(UserManagerSingleton.getInstance().getUserIncomeMultipleAttributeFromCache(userId) * (cfgVehicleDTO.getExtraRewardValue()));
        }else if(arg.getSettlementRole() == RoleEnum.pet.getRoleType()){
            // 宠物结算
            if(arg.getSettlementType() == 1){
                // 在线结算，最终收益 = 宠物等级对应的收益
                moneyIncome = userAttributeDTO.getPetLevel() + 1;
            }else{
                // 宠物离线收益
                moneyIncome = petOfflineIncome(userDTO,userAttributeDTO,arg.getMultiple());
            }
            // vip等级效果中是否有宠物搬运金额加成
            UserVipDTO userVipDTO = UserManagerSingleton.getInstance().getUserVipFromCache(userId);
            if(userVipDTO != null){
                CfgVipDTO cfgVipDTO = CfgManagerSingleton.getInstance().getCfgVipByVipLevelFromCache(userVipDTO.getVipLevel());
                if(cfgVipDTO != null){
                    for(int i=0;i<cfgVipDTO.getJsonArrayEffectAttributeInfo().size();i++){
                        JSONObject jsonEffectAttributeInfo = cfgVipDTO.getJsonArrayEffectAttributeInfo().getJSONObject(i);
                        int attributeType = jsonEffectAttributeInfo.getIntValue("attributeType");
                        if(attributeType == AttributeEnum.petSettlementMoney.getAttributeType()){
                            float multiple = jsonEffectAttributeInfo.getFloatValue("multiple");
                            moneyIncome *= multiple;
                        }
                    }
                }
            }
        }
        long finalMoney = userDTO.getMoney() + moneyIncome;
        long finalMoneyHistory = userDTO.getMoneyHistory() + moneyIncome;
        userDTO.setMoney(finalMoney);
        userDTO.setMoneyHistory(finalMoneyHistory);

        /** 同步金钱数量（推送）**/
        pushPbService.moneySync(userId);

        log.info("SettlementExecutor::executor:userId = {},settlementResPb = {},end",userId,settlementResPb);
        return settlementResPb;
    }

    // 宠物离线收益
    public long petOfflineIncome(UserDTO userDTO,UserAttributeDTO userAttributeDTO,int multiple){
        // 离线结算，最终收益 = (latestLoginTime - latestLogoutTime)/petFinishJobTime * 宠物等级对应的收益
        long offlineTime = (userDTO.getLatestLoginTime().getTime() - userDTO.getLatestLogoutTime().getTime())/1000;
        int petOfflineIncomeMaxTime = Integer.valueOf(CfgManagerSingleton.getInstance().getCfgGlobalByKeyFromCache("petOfflineIncomeMaxTime").getValueName()) * 3600;
        if(offlineTime >= petOfflineIncomeMaxTime) offlineTime = petOfflineIncomeMaxTime;
        int finishJobCount = (int)offlineTime / (Integer.valueOf(CfgManagerSingleton.getInstance().getCfgGlobalByKeyFromCache("petFinishJobTime").getValueName()));
        long offlineIncome = multiple * finishJobCount * (userAttributeDTO.getPetLevel() + 1);

        return offlineIncome;
    }
}

package logic.server.service.impl.action;

import com.alibaba.fastjson.JSONObject;
import common.pb.pb.SettlementReqPb;
import common.pb.pb.SettlementResPb;
import logic.server.dto.CfgVehicleDTO;
import logic.server.dto.CfgVehicleNewDTO;
import logic.server.dto.CfgVipDTO;
import logic.server.dto.UserAttributeDTO;
import logic.server.dto.UserDTO;
import logic.server.dto.UserVehicleDTO;
import logic.server.dto.UserVehicleNewDTO;
import logic.server.dto.UserVipDTO;
import logic.server.enums.AttributeEnum;
import logic.server.enums.RoleEnum;
import logic.server.service.IPushPbService;
import logic.server.singleton.CfgManagerSingleton;
import logic.server.singleton.UserManagerSingleton;
import lombok.extern.slf4j.Slf4j;
import org.nfunk.jep.JEP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

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
            if(arg.getFlyMoney() == 0){
                // 老结算模式
                // 主角结算-载具容量满
                // 最终收益 = 角色收益倍数属性 * （载具额外奖励数值）
                UserVehicleDTO userVehicleDTO = UserManagerSingleton.getInstance().getUserUsingVehicleByIdFromCache(userId);
                CfgVehicleDTO cfgVehicleDTO = CfgManagerSingleton.getInstance().getCfgVehicleByIdFromCache(userVehicleDTO.getVehicleId());
                moneyIncome = (long)(UserManagerSingleton.getInstance().getUserIncomeMultipleAttributeFromCache(userId) * (cfgVehicleDTO.getExtraRewardValue()));
            }else{
                // 新结算模式：飞钱模式
                moneyIncome = arg.getFlyMoney();
            }
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
        }else if(arg.getSettlementRole() == RoleEnum.vehicleNew.getRoleType()){
            // 载具（新）结算
            boolean isOfflineIncome = true;
            int multipleByAd = arg.getMultiple();
            if(arg.getSettlementType() == 1){
                isOfflineIncome = false;
            }
            if(multipleByAd == 0) multipleByAd = 1;

            moneyIncome = vehicleNewIncome(userId,multipleByAd,isOfflineIncome);
        }
        long finalMoney = userDTO.getMoney() + moneyIncome;
        long finalMoneyHistory = userDTO.getMoneyHistory() + moneyIncome;
        userDTO.setMoney(finalMoney);
        userDTO.setMoneyHistory(finalMoneyHistory);

        /** 同步金钱数量（推送）**/
        pushPbService.moneySync(userId);

        log.info("SettlementExecutor::executor:userId = {},settlementResPb = {},moneyIncome = {},finalMoney = {},end",userId,settlementResPb,moneyIncome,finalMoney);
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

    /**
     * 载具（新）收益
     * @param userId
     * @param multipleByAd:倍数（来源于是否看广告，客户端发送，默认是1倍）
     * @param isOfflineIncome：是否是离线收益
     * @return
     */
    public long vehicleNewIncome(long userId,int multipleByAd,boolean isOfflineIncome){
        long income = 0;

        try{
            int finishJobCount = 1;// 在线收益，收益次数固定为1次
            if(isOfflineIncome){
                // 离线收益：收益次数
                UserDTO userDTO = UserManagerSingleton.getInstance().getUserByIdFromCache(userId);
                long offlineTime = (userDTO.getLatestLoginTime().getTime() - userDTO.getLatestLogoutTime().getTime())/1000;
                int vehicleNewOfflineIncomeMaxTime = Integer.valueOf(CfgManagerSingleton.getInstance().getCfgGlobalByKeyFromCache("petOfflineIncomeMaxTime").getValueName()) * 3600;
                if(offlineTime >= vehicleNewOfflineIncomeMaxTime) offlineTime = vehicleNewOfflineIncomeMaxTime;
                finishJobCount = (int)offlineTime / (Integer.valueOf(CfgManagerSingleton.getInstance().getCfgGlobalByKeyFromCache("petFinishJobTime").getValueName()));
            }
            // 遍历所有已解锁的载具（新）
            long totalVehicleNewBaseIncome = 0;// 所有载具（新）基础收益综合
            Map<Integer, UserVehicleNewDTO> userVehicleNewDTOMap = UserManagerSingleton.getInstance().getUserVehicleNewMapByIdFromCache(userId);
            for(Map.Entry<Integer,UserVehicleNewDTO> entry : userVehicleNewDTOMap.entrySet()){
                UserVehicleNewDTO userVehicleNewDTO = entry.getValue();
                if(!userVehicleNewDTO.isUnlocked()) continue;
                CfgVehicleNewDTO cfgVehicleNewDTO = CfgManagerSingleton.getInstance().getCfgVehicleNewByIdFromCache(userVehicleNewDTO.getVehicleId());
                String incomeFormula = cfgVehicleNewDTO.getIncomeFormula();
                incomeFormula = incomeFormula.replace("`lv`","lv");
                JEP jep = new JEP();
                jep.addVariable("lv",userVehicleNewDTO.getLevel());
                jep.parseExpression(incomeFormula);
                totalVehicleNewBaseIncome += (long)jep.getValue();
            }

            float userIncomeMultiple = UserManagerSingleton.getInstance().getUserIncomeMultipleAttributeFromCache(userId);
            // 最终收益 = 所有解锁载具（新）基础收益总和 * 广告收益倍数 * 用户收益倍数（装备 + buffTool + vip） * 完成次数
            float finalIncome = totalVehicleNewBaseIncome * multipleByAd * userIncomeMultiple * finishJobCount;
            income = (long)finalIncome;
            log.info("SettlementExecutor::vehicleNewIncome:userId = {},isOfflineIncome = {}," +
                    "totalVehicleNewBaseIncome = {},multipleByAd = {},userIncomeMultiple = {},finishJobCount = {}",
                    userId,isOfflineIncome,totalVehicleNewBaseIncome,multipleByAd,userIncomeMultiple,finishJobCount);
        }catch (Exception e){
            log.info("SettlementExecutor::vehicleNewIncome:userId = {},isOfflineIncome = {},message = {},载具（新）收益计算异常",userId,isOfflineIncome,e.getMessage());
        }

        return income;
    }
}

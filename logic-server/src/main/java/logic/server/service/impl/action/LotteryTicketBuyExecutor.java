package logic.server.service.impl.action;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.iohao.game.action.skeleton.core.exception.MsgException;
import common.pb.enums.ErrorCodeEnum;
import common.pb.pb.LotteryTicketBuyReqPb;
import common.pb.pb.LotteryTicketBuyResPb;
import common.pb.pb.LotteryTicketInfoPb;
import logic.server.dto.CfgLotteryTicketDTO;
import logic.server.dto.UserDTO;
import logic.server.service.IPushPbService;
import logic.server.singleton.CfgManagerSingleton;
import logic.server.singleton.UserManagerSingleton;
import lombok.extern.slf4j.Slf4j;
import org.nfunk.jep.JEP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

@Slf4j
@Service
public class LotteryTicketBuyExecutor implements BaseExecutor<LotteryTicketBuyReqPb, LotteryTicketBuyResPb,Long> {
    @Autowired
    private IPushPbService pushPbService;

    @Override
    public LotteryTicketBuyResPb executor(LotteryTicketBuyReqPb arg, Long userId) throws MsgException {
        log.info("LotteryTicketBuyExecutor::executor:userId = {},arg = {},start",userId,arg);
        LotteryTicketBuyResPb lotteryTicketBuyResPb = new LotteryTicketBuyResPb();

        CfgLotteryTicketDTO cfgLotteryTicketDTO = CfgManagerSingleton.getInstance().getCfgLotteryTicketByFaceValueFromCache(arg.getFaceValue());
        // 彩票费用
        UserDTO userDTO = UserManagerSingleton.getInstance().getUserByIdFromCache(userId);
        if(!arg.isBoughtByAd && userDTO.getMoney() < cfgLotteryTicketDTO.getFaceValue()){
            lotteryTicketBuyResPb.setCode(ErrorCodeEnum.moneyCostNotEnough.getCode()).setMessage(ErrorCodeEnum.moneyCostNotEnough.getMsg());
            log.info("LotteryTicketBuyExecutor::executor:userId = {},attributeLevelUpResPb = {},end",userId, lotteryTicketBuyResPb);
            return lotteryTicketBuyResPb;
        }

        // 创建彩票
        createLotteryTicket(lotteryTicketBuyResPb.getLotteryTicketInfoPbList(),arg.getAmount(),cfgLotteryTicketDTO, userDTO);

        long finalMoney = userDTO.getMoney() - cfgLotteryTicketDTO.getFaceValue();
        userDTO.setMoney(finalMoney);
        /** 同步金钱数量（推送）**/
        pushPbService.moneySync(userId);

        log.info("LotteryTicketBuyExecutor::executor:userId = {},buyLotteryTicketResPb = {},end",userId, lotteryTicketBuyResPb);
        return lotteryTicketBuyResPb;
    }

    private void createLotteryTicket(List<LotteryTicketInfoPb> lotteryTicketInfoPbList,int amount,CfgLotteryTicketDTO cfgLotteryTicketDTO,UserDTO userDTO){
        JSONArray jsonArrayFirstTimeBonusFormula = cfgLotteryTicketDTO.getJsonArrayFirstTimeBonusFormula();
        JSONArray jsonArrayWinningBonusFormula = cfgLotteryTicketDTO.getJsonArrayWinningBonusFormula();
        String losingBonusFormula = cfgLotteryTicketDTO.getLosingBonusFormula();
        int losingBonusMin = Integer.valueOf(losingBonusFormula.substring(0,losingBonusFormula.indexOf("-")));
        int losingBonusMax = Integer.valueOf(losingBonusFormula.substring(losingBonusFormula.indexOf("-")+1));

        for(int i=0;i<amount;i++){
            LotteryTicketInfoPb lotteryTicketInfoPb = new LotteryTicketInfoPb();

            // 面值
            lotteryTicketInfoPb.setFaceValue(cfgLotteryTicketDTO.getFaceValue());
            // 中奖号码
            Random r = new Random();
            int randomWinningNumber = r.nextInt(9) + 1;
            String winningNumber = String.format("0%d",randomWinningNumber);
            lotteryTicketInfoPb.setWinningNumber(winningNumber);
            // 我的号码
            int winningCount = 0;// 本张彩票中奖数量
            String winningNumberFormula = cfgLotteryTicketDTO.getWinningNumberFormula();
            boolean isFirstTime = userDTO.getBoughtLotteryTicketCount() == 0 ? true : false;
            int randomFirstTimeWinningPosition = r.nextInt(9);// 随机中奖号码位置（第一次购买彩票所需）
            for(int j=0;j<9;j++){
                if(isFirstTime){
                    JSONObject jsonMyNumber = new JSONObject();
                    if(randomFirstTimeWinningPosition == j){
                        // 第一次购买彩票
                        Float startPercent = .0f;
                        Float endPercent = .0f;
                        float randomFirstTimeWinningBonusPercent = r.nextFloat() * 100;// 随机中奖金额概率
                        for(int a=0;a<jsonArrayFirstTimeBonusFormula.size();a++){
                            // 中奖金额计算
                            JSONObject jsonFirstTimeBonusFormula = jsonArrayFirstTimeBonusFormula.getJSONObject(a);
                            Float percent = jsonFirstTimeBonusFormula.getFloatValue("percent");
                            if(startPercent == .0f){
                                endPercent = percent;
                            }else{
                                endPercent += percent;
                            }
                            if(randomFirstTimeWinningBonusPercent >= startPercent && randomFirstTimeWinningBonusPercent <= endPercent){
                                long bonus = jsonFirstTimeBonusFormula.getBigDecimal("bonus").longValue();
                                jsonMyNumber.put("number",winningNumber);
                                jsonMyNumber.put("isWinning",true);
                                jsonMyNumber.put("bonus",bonus);
                                winningCount++;
                                break;
                            }
                            startPercent = endPercent;
                        }
                    }else{
                        // 未中奖号码
                        int randomLosingNumber = r.nextInt(9) + 1;
                        while (randomLosingNumber == randomWinningNumber) {
                            randomLosingNumber = r.nextInt(9) + 1;
                        }
                        jsonMyNumber.put("number",String.format("0%d",randomLosingNumber));
                        jsonMyNumber.put("isWinning",false);
                        int randomBaseBonus = losingBonusMin + r.nextInt(losingBonusMax - losingBonusMin + 1) ;
                        long bonus = randomBaseBonus * cfgLotteryTicketDTO.getFaceValue();
                        jsonMyNumber.put("bonus",bonus);
                    }
                    lotteryTicketInfoPb.getMyNumberList().add(jsonMyNumber.toJSONString());
                }else{
                    // 常规购买彩票
                    JEP jep = new JEP();
                    jep.addVariable("n",winningCount);
                    jep.parseExpression(winningNumberFormula);
                    int winningPercent = (int)jep.getValue();
                    int randomWinningPercent = r.nextInt(100) + 1;
                    JSONObject jsonMyNumber = new JSONObject();
                    if( randomWinningPercent <= winningPercent){
                        // 中奖号码信息
                        jsonMyNumber.put("number",winningNumber);
                        jsonMyNumber.put("isWinning",true);
                        // 计算奖金
                        Float startPercent = .0f;
                        Float endPercent = .0f;
                        float randomWinningBonusPercent = r.nextFloat() * 100;
                        for(int b=0;b<jsonArrayWinningBonusFormula.size();b++){
                            JSONObject jsonWinningBonusFormula = jsonArrayWinningBonusFormula.getJSONObject(b);
                            Float percent = jsonWinningBonusFormula.getFloatValue("percent");
                            if(startPercent == .0f){
                                endPercent = percent;
                            }else{
                                endPercent += percent;
                            }
                            if(randomWinningBonusPercent >= startPercent && randomWinningBonusPercent <= endPercent){
                                String range = jsonWinningBonusFormula.getString("range");
                                Float winningBonusMin = Float.valueOf(range.substring(0,range.indexOf("-")));
                                Float winningBonusMax = Float.valueOf(range.substring(range.indexOf("-")+1));
                                float randomWinningBaseBonus = winningBonusMin + r.nextFloat() * winningBonusMax;
                                long bonus = (long)(randomWinningBaseBonus * cfgLotteryTicketDTO.getFaceValue());
                                jsonMyNumber.put("bonus",bonus);
                                break;
                            }
                            startPercent = endPercent;
                        }
                        winningCount++;
                    }else{
                        // 未中奖号码信息
                        int randomLosingNumber = r.nextInt(9) + 1;
                        while (randomLosingNumber == randomWinningNumber) {
                            randomLosingNumber = r.nextInt(9) + 1;
                        }
                        jsonMyNumber.put("number",String.format("0%d",randomLosingNumber));
                        jsonMyNumber.put("isWinning",false);
                        int randomBaseBonus = losingBonusMin + r.nextInt(losingBonusMax - losingBonusMin + 1) ;
                        long bonus = randomBaseBonus * cfgLotteryTicketDTO.getFaceValue();
                        jsonMyNumber.put("bonus",bonus);
                    }
                    lotteryTicketInfoPb.getMyNumberList().add(jsonMyNumber.toJSONString());
                }
            }

            userDTO.setBoughtLotteryTicketCount(userDTO.getBoughtLotteryTicketCount() + 1);
            lotteryTicketInfoPbList.add(lotteryTicketInfoPb);
        }
    }
}

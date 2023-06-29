package logic.server.service.impl.action;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.annotations.JsonAdapter;
import common.pb.enums.ErrorCodeEnum;
import common.pb.pb.DrawReqPb;
import common.pb.pb.DrawResPb;
import logic.server.dto.CfgDrawDTO;
import logic.server.dto.UserDrawDTO;
import logic.server.enums.StickEnum;
import logic.server.singleton.CfgManagerSingleton;
import logic.server.singleton.UserManagerSingleton;
import lombok.extern.slf4j.Slf4j;
import org.nfunk.jep.JEP;
import org.springframework.stereotype.Service;

import java.util.Random;

@Slf4j
@Service
public class DrawExecutor implements BaseExecutor<DrawReqPb, DrawResPb,Long>{
    @Override
    public DrawResPb executor(DrawReqPb arg, Long userId){
        log.info("DrawExecutor::executor:userId = {},arg = {},start",userId,arg);
        DrawResPb drawResPb = new DrawResPb();

        UserDrawDTO userDrawDTO = UserManagerSingleton.getInstance().getUserDrawFromCache(userId);
        if(userDrawDTO == null){
            log.info("DrawExecutor::executor:userId = {},userDrawDTO is null",userId);
            drawResPb.setCode(ErrorCodeEnum.drawCommonError.getCode()).setMessage(ErrorCodeEnum.drawCommonError.getMsg());
            return drawResPb;
        }
        if(userDrawDTO.getSsqCount() >= userDrawDTO.getSsqTargetCount()){
            log.info("DrawExecutor::executor:userId = {},ssqCount = {},ssqTargetCount = {},上上签数量已达标",userId,userDrawDTO.getSsqCount(),userDrawDTO.getSsqTargetCount());
            drawResPb.setCode(ErrorCodeEnum.drawCommonError.getCode()).setMessage(ErrorCodeEnum.drawCommonError.getMsg());
            return drawResPb;
        }
        if(userDrawDTO.getRemainDrawCount() <= 0){
            log.info("DrawExecutor::executor:userId = {},remainDrawCount = {},剩余轮数小于等于0",userId,userDrawDTO.getRemainDrawCount());
            drawResPb.setCode(ErrorCodeEnum.drawCommonError.getCode()).setMessage(ErrorCodeEnum.drawCommonError.getMsg());
            return drawResPb;
        }
        CfgDrawDTO cfgDrawDTO = CfgManagerSingleton.getInstance().getCfgDrawByRoundNumberFromCache(userDrawDTO.getRoundNumber());
        if(cfgDrawDTO == null){
            log.info("DrawExecutor::executor:userId = {},roundNumber = {}, cfgDrawDTO is null",userId,userDrawDTO.getRoundNumber());
            drawResPb.setCode(ErrorCodeEnum.drawCommonError.getCode()).setMessage(ErrorCodeEnum.drawCommonError.getMsg());
            return drawResPb;
        }

        userDrawDTO.setRemainDrawCount(userDrawDTO.getRemainDrawCount() - 1).setBagUsable(true);
        if(userDrawDTO.getStickCount() == 1){
            // 单签概率
            JSONArray jsonArraySingleDrawProbability = cfgDrawDTO.getJsonArraySingleDrawProbability();
            processStick(userDrawDTO,jsonArraySingleDrawProbability.getJSONObject(0),drawResPb);
        }else{
            // 多签概率
            JSONArray jsonArrayMultipleDrawProbability = cfgDrawDTO.getJsonArrayMultipleDrawProbability();
            processStick(userDrawDTO,jsonArrayMultipleDrawProbability.getJSONObject(0),drawResPb);// 第一签
            processStick(userDrawDTO,jsonArrayMultipleDrawProbability.getJSONObject(1),drawResPb);// 第二签
        }
        drawResPb.setBagUsable(true);
        log.info("DrawExecutor::executor:userId = {},drawResPb = {},end",userId,drawResPb);
        return drawResPb;
    }

    private void processStick(UserDrawDTO userDrawDTO, JSONObject jsonDrawProbability,DrawResPb drawResPb){
        StickEnum stickEnum = drawRandom(userDrawDTO,jsonDrawProbability);
        drawResPb.getStickList().add(stickEnum.getStickType());
        if(stickEnum.getStickType() == StickEnum.ssq.getStickType()){
            userDrawDTO.setSsqCount(userDrawDTO.getSsqCount() + 1);
            userDrawDTO.setDrewCount(0);
        }else{
            userDrawDTO.setDrewCount(userDrawDTO.getDrewCount() + 1);
        }
        drawResPb.setRemainDrawCount(userDrawDTO.getRemainDrawCount()).setSsqCount(userDrawDTO.getSsqCount()).setBagUsable(userDrawDTO.isBagUsable());
    }

    private StickEnum drawRandom(UserDrawDTO userDrawDTO, JSONObject jsonSingleDrawProbability){
        Random r = new Random();
        int randomPercent = r.nextInt(100) + 1;

        // 上上签概率
        String ssqProbability = jsonSingleDrawProbability.getString("ssq");
        JEP jep = new JEP();
        jep.addVariable("n",userDrawDTO.getDrewCount());
        jep.parseExpression(ssqProbability);
        double t = jep.getValue();
        int ssqPercentMin = 1;
        int ssqPercentMax = (int)(jep.getValue());
        if(randomPercent >= ssqPercentMin && randomPercent <= ssqPercentMax){
            return StickEnum.ssq;
        }

        // 上吉签概率
        String sjqProbability = jsonSingleDrawProbability.getString("sjq");
        jep = new JEP();
        jep.addVariable("n",userDrawDTO.getDrewCount());
        jep.parseExpression(sjqProbability);
        int sjqPercentMin = ssqPercentMax;
        int sjqPercentMax = sjqPercentMin + (int)(jep.getValue());
        if(randomPercent > sjqPercentMin && randomPercent <= sjqPercentMax){
            return StickEnum.sjq;
        }

        // 中吉签概率
        String zjqProbability = jsonSingleDrawProbability.getString("zjq");
        jep = new JEP();
        jep.addVariable("n",userDrawDTO.getDrewCount());
        jep.parseExpression(zjqProbability);
        int zjqPercentMin = sjqPercentMax;
        int zjqPercentMax = zjqPercentMin + (int)(jep.getValue());
        if(randomPercent > zjqPercentMin && randomPercent <= zjqPercentMax){
            return StickEnum.zjq;
        }

        // 剩余是中平签
        return StickEnum.zpq;
    }
}

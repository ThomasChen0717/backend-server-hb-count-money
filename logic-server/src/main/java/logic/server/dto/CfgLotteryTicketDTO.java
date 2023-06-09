package logic.server.dto;

import com.alibaba.fastjson.JSONArray;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class CfgLotteryTicketDTO implements DTO{
    private static final long serialVersionUID = 1L;

    private Long id;
    private int faceValue;
    private String winningNumberFormula;
    private String firstTimeBonusFormula;
    private String winningBonusFormula;
    private String losingBonusFormula;

    public JSONArray getJsonArrayFirstTimeBonusFormula(){
        return JSONArray.parseArray(firstTimeBonusFormula);
    }
    public JSONArray getJsonArrayWinningBonusFormula(){
        return JSONArray.parseArray(winningBonusFormula);
    }
}

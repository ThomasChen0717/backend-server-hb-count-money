package admin.server.dto;

import com.alibaba.fastjson.JSONArray;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class CfgDrawDTO implements DTO{
    private static final long serialVersionUID = 1L;

    private Long id;
    private int roundNumber;
    private int drawCount;
    private int ssqTargetCount;
    private float reward;
    private float includeExtraReward;
    private float punish;
    private float bagProbability;
    private String singleDrawProbability;
    private String multipleDrawProbability;

    public JSONArray getJsonArraySingleDrawProbability(){
        return JSONArray.parseArray(singleDrawProbability);
    }

    public JSONArray getJsonArrayMultipleDrawProbability(){
        return JSONArray.parseArray(multipleDrawProbability);
    }
}

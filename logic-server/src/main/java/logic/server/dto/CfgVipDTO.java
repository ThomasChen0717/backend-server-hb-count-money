package logic.server.dto;

import com.alibaba.fastjson.JSONArray;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class CfgVipDTO implements DTO{
    private static final long serialVersionUID = 1L;

    private long id;
    private int vipLevel;
    private int conditionCount;
    private String effectAttributeInfo;

    public JSONArray getJsonArrayEffectAttributeInfo(){
        return JSONArray.parseArray(effectAttributeInfo);
    }
}

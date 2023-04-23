package logic.server.dto;

import com.alibaba.fastjson.JSONArray;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class CfgBuffToolDTO implements DTO{
    private static final long serialVersionUID = 1L;

    private long id;
    private int buffToolId;
    private String effectAttributeInfo;
    private int durations;

    public JSONArray getJsonArrayEffectAttributeInfo(){
        return JSONArray.parseArray(effectAttributeInfo);
    }
}

package logic.server.dto;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class CfgGlobalDTO implements DTO{
    private static final long serialVersionUID = 1L;

    private long id;
    private String keyName;
    private String valueName;
    private String remark;
}

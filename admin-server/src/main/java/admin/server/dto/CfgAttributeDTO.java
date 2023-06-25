    package admin.server.dto;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class CfgAttributeDTO implements DTO {
    private static final long serialVersionUID = 1L;

    private long id;
    private int attributeType;
    private String attributeName;
    private String attributeLevelUpFormula;
    private String attributeEffectFormula;
}

package logic.server.dto;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class CfgEquipmentDTO implements DTO{
    private static final long serialVersionUID = 1L;

    private long id;
    private int equipmentId;
    private String equipmentName;
    private int unlockConditionType;
    private int unlockConditionCount;
    private int effectAttributeType;
    private float effectAttributeMultiple;
    private String effectAttributeRemark;
}

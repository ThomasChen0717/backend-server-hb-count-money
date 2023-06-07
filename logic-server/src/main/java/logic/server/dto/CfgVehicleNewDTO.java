package logic.server.dto;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class CfgVehicleNewDTO implements DTO{
    private static final long serialVersionUID = 1L;

    private long id;
    private int vehicleId;
    private String vehicleName;
    private int preConditionChallengeType;
    private int preConditionChallengeId;
    private int unlockConditionType;
    private long unlockConditionCount;
    private int showIndex;
    private int levelMax;
    private String levelUpFormula;
    private String incomeFormula;
    private String resourceName;
}

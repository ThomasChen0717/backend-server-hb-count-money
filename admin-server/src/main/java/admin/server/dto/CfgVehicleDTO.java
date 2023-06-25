package admin.server.dto;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class CfgVehicleDTO implements DTO {
    private static final long serialVersionUID = 1L;

    private long id;
    private int vehicleId;
    private String vehicleName;
    private int unlockConditionType;
    private int unlockConditionCount;
    private int vehicleCapacity;
    private int extraRewardValue;
    private int showIndex;
    private String resourceName;
}

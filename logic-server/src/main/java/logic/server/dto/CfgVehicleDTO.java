package logic.server.dto;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class CfgVehicleDTO implements DTO{
    private static final long serialVersionUID = 1L;

    private long id;
    private int vehicleId;
    private String vehicleImg;
    private String vehicleName;
    private int conditionType;
    private int conditionCount;
    private int rewardConditionCount;
    private int rewardValue;
}

package logic.server.dto;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class UserVehicleNewDTO implements DTO{
    private static final long serialVersionUID = 1L;

    private long id;
    private long userId;
    private int vehicleId;
    private boolean isPreConditionClear;
    private int unlockConditionCurrCount;
    private boolean isUnlocked;
    private int level;
}

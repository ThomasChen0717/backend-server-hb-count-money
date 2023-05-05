package logic.server.dto;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class CfgMagnateDTO implements DTO{
    private static final long serialVersionUID = 1L;

    private long id;
    private int magnateId;
    private String magnateName;
    private int speed;
    private int targetMoneyAmount;
    private int rewardMoneyAmount;
    private int unlockVehicleId;
    private int cdTime;
    private int challengeTime;
    private int preMagnateId;
    private int showIndex;
    private String resourceName;
    private String bossWord;
}

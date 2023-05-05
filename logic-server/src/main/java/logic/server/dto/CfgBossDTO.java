package logic.server.dto;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class CfgBossDTO implements DTO{
    private static final long serialVersionUID = 1L;

    private long id;
    private int bossId;
    private String bossName;
    private int speed;
    private int targetMoneyAmount;
    private int rewardMoneyAmount;
    private int challengeTime;
    private int preBossId;
    private int showIndex;
    private String resourceName;
    private String bossWord;
}

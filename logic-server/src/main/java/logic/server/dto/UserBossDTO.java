package logic.server.dto;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class UserBossDTO implements DTO{
    private static final long serialVersionUID = 1L;

    private long id;
    private long userId;
    private int bossId;
    private boolean isUnlocked;
    private boolean isBeat;
}

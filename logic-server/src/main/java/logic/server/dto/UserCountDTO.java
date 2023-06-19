package logic.server.dto;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class UserCountDTO implements DTO{
    private static final long serialVersionUID = 1L;

    private long id;
    private int logicServerId;
    private int onlineUserCount;
    private int cacheUserCount;
}

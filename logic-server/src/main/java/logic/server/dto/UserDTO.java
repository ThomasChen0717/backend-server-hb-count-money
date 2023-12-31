package logic.server.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * @author mark
 * @date 2023-04-12
 */
@Data
@Accessors(chain = true)
public class UserDTO implements DTO{
    private static final long serialVersionUID = 1L;

    private boolean isNewUser;

    private long id;
    private String name;
    private String title;
    private String loginPlatform;
    private String token;
    private String unionId;
    private String openid;
    private int onlineServerId;
    private boolean isOnline;
    private long money;
    private long moneyHistory;
    private Date latestLoginTime;
    private Date latestLogoutTime;
    private int privilegeLevel;
    private String clientVersion;
    private String firstClientVersion;
    private int selectStoneCount = 0;
    private int clickCount;
    private int boughtLotteryTicketCount;
    private Date latestDrawTime;
}

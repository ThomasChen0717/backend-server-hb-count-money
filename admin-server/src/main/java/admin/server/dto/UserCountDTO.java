package admin.server.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDate;
import java.time.LocalDateTime;


@Data
@Accessors(chain = true)
public class UserCountDTO {
    private static final long serialVersionUID = 1L;

    private long id;
    private int logicServerId;
    private String onlineUserCount;
    private String cacheUserCount;
    private LocalDateTime createTime;
    private String createTimeString;
}

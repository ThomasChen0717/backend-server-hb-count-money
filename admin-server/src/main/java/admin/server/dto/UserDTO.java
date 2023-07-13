package admin.server.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * @author mark
 * @date 2023-04-12
 */
@Data
@Accessors(chain = true)
public class UserDTO implements DTO{
    private static final long serialVersionUID = 1L;
    private long id;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}

package logic.server.dto;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author mark
 * @date 2023-04-12
 */
@Data
@Accessors(chain = true)
public class UserDTO implements DTO{
    private static final long serialVersionUID = 1L;

    private long id;
    private String name;
    private String loginPlatform;
    private String token;
    private String unionId;
    private String openid;
    private long money;
}

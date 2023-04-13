package logic.server.dto;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author mark
 * @date 2023-04-12
 */
@Data
@Accessors(chain = true)
public class UsersDTO implements DTO{
    private static final long serialVersionUID = 1L;

    private Long id;

    private String name;
}

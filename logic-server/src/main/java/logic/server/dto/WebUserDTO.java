package logic.server.dto;


import lombok.Data;
import lombok.experimental.Accessors;


@Data
@Accessors(chain = true)
public class WebUserDTO implements DTO{
    private static final long serialVersionUID = 1L;

    private long id;
    private String username;
    private String password;
    private String role;
    private String avatar;
    private String name;
}

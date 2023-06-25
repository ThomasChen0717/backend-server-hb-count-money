package admin.server.dto;


import admin.server.dto.Model;
import lombok.Data;
import lombok.experimental.Accessors;


@Data
@Accessors(chain = true)
public class WebUserDTO implements Model {
    private static final long serialVersionUID = 1L;

    private long id;
    private String username;
    private String password;
    private String role;
    private String avatar;
    private String name;
}

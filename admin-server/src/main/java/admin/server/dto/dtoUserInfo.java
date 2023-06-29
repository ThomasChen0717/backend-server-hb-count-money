package admin.server.dto;


import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class dtoUserInfo {
    private static final long serialVersionUID = 1L;
    private long id;
    private String role;
    private String avatar;
    private String name;

}

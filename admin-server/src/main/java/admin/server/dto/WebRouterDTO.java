package admin.server.dto;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class WebRouterDTO {
    private static final long serialVersionUID = 1L;
    private long id;
    private String routeName;
    private String roles;

}

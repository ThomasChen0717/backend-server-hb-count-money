package admin.server.entity;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class RouteData {
    private String path;
    private String name;
}

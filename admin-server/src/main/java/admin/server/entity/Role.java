package admin.server.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;
@Data
@Accessors(chain = true)
public class Role {
    private String name;
    private List<RouteData> routes;
}

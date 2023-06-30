package admin.server.dto;

import admin.server.entity.RouteData;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;
@Data
@Accessors(chain = true)
public class RoleDTO {
    private String name;
    private List<RouteData> routes;
}

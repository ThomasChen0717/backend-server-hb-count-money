package logic.server.dto;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class UserVehicleDTO implements DTO{
    private static final long serialVersionUID = 1L;

    private long id;
    private long UserId;
    private String vehicleList;
}

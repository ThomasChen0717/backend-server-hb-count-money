package admin.server.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
@Data
@Accessors(chain = true)
public class UserCountFilterDTO {
    private static final long serialVersionUID = 1L;
    private LocalDateTime date;
    private String hour;
}

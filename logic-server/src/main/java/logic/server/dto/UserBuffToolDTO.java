package logic.server.dto;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class UserBuffToolDTO implements DTO{
    private static final long serialVersionUID = 1L;

    private long id;
    private long userId;
    private int buffToolId;
    private boolean isInUse;
}

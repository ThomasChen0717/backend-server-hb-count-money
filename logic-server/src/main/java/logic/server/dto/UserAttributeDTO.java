package logic.server.dto;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author mark
 * @date 2023-04-17
 */
@Data
@Accessors(chain = true)
public class UserAttributeDTO implements DTO{
    private static final long serialVersionUID = 1L;

    private long id;
    private long userId;
    private int strengthLevel = 1;
    private int physicalLevel = 1;
    private int physicalRestoreLevel = 1;
    private int enduranceLevel = 1;
    private int petLevel = 1;
}

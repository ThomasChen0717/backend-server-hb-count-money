package logic.server.po;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * @author mark
 * @date 2023-04-13
 */
@Data
@Accessors(chain = true)
public class BaseEntity implements Serializable {
    private Date createTime;
    private Date updateTime;
}

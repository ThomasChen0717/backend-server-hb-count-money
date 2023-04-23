package logic.server.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@TableName("t_user_buff_tool")
public class UserBuffToolPO extends BaseEntity{
    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private long id;

    /**
     * 角色id
     */
    private long userId;

    /**
     * buffToolId
     */
    private long buffToolId;

    /**
     * 是否使用中：0 未使用 1 使用中
     */
    private boolean isInUse;
}

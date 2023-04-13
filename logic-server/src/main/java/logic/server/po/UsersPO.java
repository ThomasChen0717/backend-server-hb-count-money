package logic.server.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author mark
 * @date 2023-04-12
 */
@Data
@Accessors(chain = true)
@TableName("t_users")
public class UsersPO extends BaseEntity{
    private static final long serialVersionUID = 1L;

    /**
     * 用户id（也是主键id）
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 名字
     */
    private String name;
}

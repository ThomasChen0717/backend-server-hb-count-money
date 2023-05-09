package logic.server.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author mark
 * @date 2023-04-17
 */
@Data
@Accessors(chain = true)
@TableName("t_user_attribute")
public class UserAttributePO extends BaseEntity{
    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 角色id
     */
    private long userId;

    /**
     * 力量等级
     */
    private int strengthLevel;

    /**
     * 体力等级
     */
    private int physicalLevel;

    /**
     * 体力恢复等级
     */
    private int physicalRestoreLevel;

    /**
     * 耐力等级
     */
    private int enduranceLevel;

    /**
     * 宠物等级
     */
    private int petLevel;
}

package logic.server.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@TableName("t_user_equipment")
public class UserEquipmentPO extends BaseEntity{
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
     * 装备id
     */
    private int equipmentId;

    /**
     * 是否使用中：0 未使用 1 使用中
     */
    private boolean isInUse;

    /**
     * 当前解锁条件数值
     */
    private int unlockConditionCurrCount;

    /**
     * 是否已解除锁定：0 未解锁 1 已解锁
     */
    private boolean isUnlocked;
}

package logic.server.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@TableName("t_user_magnate")
public class UserMagnatePO extends BaseEntity{
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
     * 富豪id
     */
    private int magnateId;

    /**
     * 是否已解除锁定：0 未解锁 1 已解锁
     */
    private boolean isUnlocked;

    /**
     * 是否已击败：0 未击败 1 已击败
     */
    private boolean isBeat;
}

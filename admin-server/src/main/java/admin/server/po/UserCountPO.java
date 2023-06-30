package admin.server.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;


@Data
@Accessors(chain = true)
@TableName("t_user_count")
public class UserCountPO extends BaseEntity {
    /**
     * 主键id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 逻辑服id
     */
    private int logicServerId;

    /**
     * 在线人数
     */
    private int onlineUserCount;

    /**
     * 缓存人数
     */
    private int cacheUserCount;

    /**
     * 时间
     */
    private Date createTime;
}

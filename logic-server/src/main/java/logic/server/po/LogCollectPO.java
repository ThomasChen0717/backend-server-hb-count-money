package logic.server.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@TableName("t_log_collect")
public class LogCollectPO extends BaseEntity{
    /**
     * 主键id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户id
     */
    private long userId;

    /**
     * 客户端版本号
     */
    private String clientVersion;

    /**
     * 日志信息
     */
    private String logInfo;
}

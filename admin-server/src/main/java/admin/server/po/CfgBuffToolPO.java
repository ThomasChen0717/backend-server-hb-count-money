package admin.server.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@TableName("t_cfg_buff_tool")
public class CfgBuffToolPO extends BaseEntity {
    /**
     * 主键id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * buffToolId
     */
    private int buffToolId;

    /**
     * Buff名
     */
    private String name;

    /**
     * 影响属性信息
     */
    private String effectAttributeInfo;

    /**
     * 持续时间，单位秒
     */
    private int durations;
}

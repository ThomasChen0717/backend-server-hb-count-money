package logic.server.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@TableName("t_cfg_vip")
public class CfgVipPO extends BaseEntity{
    /**
     * 主键id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * vip等级
     */
    private int vipLevel;

    /**
     * 达成条件：观看视频次数
     */
    private int conditionCount;

    /**
     * 影响属性信息
     */
    private String effectAttributeInfo;
}

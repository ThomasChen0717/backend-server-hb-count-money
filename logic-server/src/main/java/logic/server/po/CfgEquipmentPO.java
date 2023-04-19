package logic.server.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@TableName("t_cfg_equipment")
public class CfgEquipmentPO extends BaseEntity{
    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private long id;

    /**
     * 装备id
     */
    private int equipmentId;

    /**
     * 装备名称
     */
    private String equipmentName;

    /**
     * 解锁条件类型：0 广告 1 金钱 2 挑战boss
     */
    private int unlockConditionType;

    /**
     * 解锁条件数值（如是挑战boss类型，此值为boss id）
     */
    private int unlockConditionCount;

    /**
     * 影响属性类型
     */
    private int effectAttributeType;

    /**
     * 影响属性公式
     */
    private float effectAttributeMultiple;

    /**
     * 影响属性说明
     */
    private String effectAttributeRemark;
}

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
    private Long id;

    /**
     * 装备id
     */
    private int equipmentId;

    /**
     * 装备名称
     */
    private String equipmentName;

    /**
     * 解锁条件类型：0 广告 1 金钱
     */
    private int unlockConditionType;

    /**
     * 解锁条件数值
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

    /**
     * 显示顺序：值越小展示越靠前
     */
    private int showIndex;

    /**
     * 前置装备条件：0表示没有开启前置装备条件
     */
    private int preEquipmentId;

    /**
     * 资源名称
     */
    private String resourceName;

    /**
     * 挑战榜类型：0 富豪 1 BOSS
     */
    private int preConditionChallengeType;

    /**
     * 挑战富豪或者BOSS id
     */
    private int preConditionChallengeId;
}

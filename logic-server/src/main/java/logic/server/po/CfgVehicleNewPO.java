package logic.server.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@TableName("t_cfg_vehicle_new")
public class CfgVehicleNewPO extends BaseEntity{
    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 载具id
     */
    private int vehicleId;

    /**
     * 载具名称
     */
    private String vehicleName;

    /**
     * 挑战榜类型：0 富豪 1 BOSS
     */
    private int preConditionChallengeType;

    /**
     * 挑战富豪或者BOSS id
     */
    private int preConditionChallengeId;

    /**
     * 解锁条件类型：0 广告 1 金钱
     */
    private int unlockConditionType;

    /**
     * 解锁条件数值
     */
    private long unlockConditionCount;

    /**
     * 显示顺序：值越小展示越靠前
     */
    private int showIndex;

    /**
     * 最大等级
     */
    private int levelMax;

    /**
     * 升级费用公式
     */
    private String levelUpFormula;

    /**
     * 收益公式
     */
    private String incomeFormula;

    /**
     * 资源名称
     */
    private String resourceName;
}

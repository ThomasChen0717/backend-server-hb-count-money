package logic.server.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@TableName("t_cfg_vehicle")
public class CfgVehiclePO extends BaseEntity{
    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private long id;

    /**
     * 载具类型
     */
    private int vehicleId;

    /**
     * 载具名称
     */
    private String vehicleName;

    /**
     * 载具图片
     */
    private String vehicleImg;

    /**
     * 条件类型：0 广告 1 金钱 2 挑战boss
     */
    private int conditionType;

    /**
     * 条件数值
     */
    private int conditionCount;

    /**
     * 奖励条件数值
     */
    private int rewardConditionCount;

    /**
     * 奖励数值
     */
    private int rewardValue;
}

package logic.server.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@TableName("t_cfg_magnate")
public class CfgMagnatePO extends BaseEntity{
    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * magnateId
     */
    private int magnateId;

    /**
     * magnateName
     */
    private String magnateName;

    /**
     * 速度值，以每秒计
     */
    private int speed;

    /**
     * 目标金钱数量
     */
    private int targetMoneyAmount;

    /**
     * 奖励金钱数量
     */
    private int rewardMoneyAmount;

    /**
     * 解锁载具id，0 说明不解锁任何载具
     */
    private int unlockVehicleId;

    /**
     * 冷却时间，单位秒
     */
    private int cdTime;

    /**
     * 挑战时间
     */
    private int challengeTime;

    /**
     * 前置富豪id，0 表示没有前置富豪条件
     */
    private int preMagnateId;

    /**
     * 展示顺序：越小显示越靠前
     */
    private int showIndex;

    /**
     * 资源名称
     */
    private String resourceName;

    /**
     * 话语文本
     */
    private String bossWord;
}

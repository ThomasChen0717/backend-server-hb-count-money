package logic.server.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@TableName("t_cfg_boss")
public class CfgBossPO extends BaseEntity{
    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * bossId
     */
    private int bossId;

    /**
     * bossName
     */
    private String bossName;

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
     * 挑战时间
     */
    private int challengeTime;

    /**
     * 前置bossId，0 表示没有前置boss条件
     */
    private int preBossId;

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

    /**
     * 0 不固定 1固定
     */
    private int fixed;
}

package admin.server.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@TableName("t_cfg_draw")
public class CfgDrawPO extends BaseEntity{
    /**
     * 主键id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 回合数
     */
    private int roundNumber;

    /**
     * 抽签次数
     */
    private int drawCount;

    /**
     * 上上签目标数
     */
    private int ssqTargetCount;

    /**
     * 胜利奖励：资产的倍数
     */
    private float reward;

    /**
     * 包含额外奖励：资产的倍数
     */
    private float includeExtraReward;

    /**
     * 失败惩罚：资产的倍数
     */
    private float punish;

    /**
     * 轮数+1概率：剩余的概率就是上上签-1
     */
    private float bagProbability;

    /**
     * 抽签概率：单签
     */
    private String singleDrawProbability;

    /**
     * 抽签概率：多签
     */
    private String multipleDrawProbability;
}

package admin.server.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@TableName("t_cfg_lottery_ticket")
public class CfgLotteryTicketPO extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 面值
     */
    private int faceValue;

    /**
     * 中奖号码公式
     */
    private String winningNumberFormula;

    /**
     * 第一次玩彩票奖金公式
     */
    private String firstTimeBonusFormula;

    /**
     * 常规中奖金额公式
     */
    private String winningBonusFormula;

    /**
     * 常规未中奖金额公式
     */
    private String losingBonusFormula;
}

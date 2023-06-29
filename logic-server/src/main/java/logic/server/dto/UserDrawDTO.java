package logic.server.dto;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class UserDrawDTO implements DTO{
    private static final long serialVersionUID = 1L;

    private long userId;
    /** 回合数 **/
    private int roundNumber;
    /** 剩余轮数 **/
    private int remainDrawCount;
    /** 上上签数量 **/
    private int ssqCount = 0;
    /** 上上签目标数量 **/
    private int ssqTargetCount;
    /** 锦囊是否可用 **/
    private boolean isBagUsable = false;
    /** 出签数量 **/
    private int stickCount = 1;
    /** 累计抽签次数：抽到上上签后重置为0 **/
    private int drewCount = 0;
}

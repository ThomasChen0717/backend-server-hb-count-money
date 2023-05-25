package logic.server.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * @author mark
 * @date 2023-04-12
 */
@Data
@Accessors(chain = true)
@TableName("t_user")
public class UserPO extends BaseEntity{
    private static final long serialVersionUID = 1L;

    /**
     * 角色id（也是主键id）
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 名字
     */
    private String name;

    /**
     * 称号
     */
    private String title;

    /**
     * 登录平台类型
     */
    private String loginPlatform;

    /**
     * token
     */
    private String token;

    /**
     * 唯一标识符
     */
    private String unionId;

    /**
     * openid
     */
    private String openid;

    /**
     * 当前所在逻辑服id:0 表示当前不在线
     */
    private int onlineServerId;

    /**
     * 金钱数量
     */
    private long money;

    /**
     * 历史金钱数量
     */
    private long moneyHistory;

    /**
     * 最近一次上线时间
     */
    private Date latestLoginTime;

    /**
     * 最近一次下线时间
     */
    private Date latestLogoutTime;

    /**
     * 权限等级：0 普通 1 GM
     */
    private int privilegeLevel;

    /**
     * 客户端版本号
     */
    private String clientVersion;

    /**
     * 累积选择石头次数
     */
    private int selectStoneCount;

    /**
     * 连续点击次数
     */
    private int clickCount;
}

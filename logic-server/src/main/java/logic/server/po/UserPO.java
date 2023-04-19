package logic.server.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

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
     * 用户id（也是主键id）
     */
    @TableId(value = "id", type = IdType.AUTO)
    private long id;

    /**
     * 名字
     */
    private String name;

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
     * 金钱数量
     */
    private long money;

    /**
     * 历史金钱数量
     */
    private long moneyHistory;
}

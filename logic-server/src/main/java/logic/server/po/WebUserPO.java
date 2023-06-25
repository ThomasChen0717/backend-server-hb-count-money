package logic.server.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
@TableName("t_web_user")
public class WebUserPO extends BaseEntity{
    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 用户权限: admin 管理员 Editor 编辑 Visitor 访客
     */
    private String role;

    /**
     * 昵称
     */
    private String name;

    /**
     * 头像
     */
    private String avatar;
}

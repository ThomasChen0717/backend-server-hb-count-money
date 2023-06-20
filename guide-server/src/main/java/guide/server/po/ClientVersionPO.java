package guide.server.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@TableName("t_client_version")
public class ClientVersionPO extends BaseEntity{
    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 客户端版本号
     */
    private String clientVersion;

    /**
     * 0：未审核或审核中 1 审核已通过
     */
    private boolean isVerified;

    /**
     * 审核服域名
     */
    private String verificationServer;

    /**
     * 正式服域名
     */
    private String releaseServer;

    /**
     * 审核服预登录域名
     */
    private String verificationPreLoginServer;

    /**
     * 正式服预登录域名
     */
    private String releasePreLoginServer;
}

package admin.server.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@TableName("t_client_version")
public class ClientVersionPO extends BaseEntity{
    /**
     * 主键id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private long id;

    /**
     * 客户端版本
     */
    private String clientVersion;

    /**
     * 已审核
     */
    private int isVerified;

    /**
     * 审核服
     */
    private String verificationServer;

    /**
     * 正式服
     */
    private String releaseServer;

    /**
     * 审核未登录服
     */
    private String verificationPreLoginServer;

    /**
     * 正式未登录服
     */
    private String releasePreLoginServer;
}

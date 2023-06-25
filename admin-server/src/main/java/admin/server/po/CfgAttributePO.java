package admin.server.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@TableName("t_cfg_attribute")
public class CfgAttributePO extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 属性名称
     */
    private int attributeType;

    /**
     * 属性名称
     */
    private String attributeName;

    /**
     * 属性升级公式
     */
    private String attributeLevelUpFormula;

    /**
     * 属性效果公式
     */
    private String attributeEffectFormula;
}

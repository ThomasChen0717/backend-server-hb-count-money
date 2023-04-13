package logic.server.po;

import com.alibaba.fastjson2.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * @author mark
 * @date 2023-04-13
 */
@Data
@Accessors(chain = true)
public class BaseEntity implements Serializable {
    private Date createTime;
    private Date updateTime;
}

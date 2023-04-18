package logic.server.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import logic.server.po.UserAttributePO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserAttributeMapper extends BaseMapper<UserAttributePO> {
}

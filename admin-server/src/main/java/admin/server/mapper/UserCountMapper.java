package admin.server.mapper;

import admin.server.po.UserCountPO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserCountMapper extends BaseMapper<UserCountPO> {
}

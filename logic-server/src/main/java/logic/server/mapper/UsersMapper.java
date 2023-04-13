package logic.server.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import logic.server.po.UsersPO;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author mark
 * @date 2023-04-12
 */
@Mapper
public interface UsersMapper extends BaseMapper<UsersPO> {
}

package logic.server.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import logic.server.po.UserVehicleNewPO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserVehicleNewMapper extends BaseMapper<UserVehicleNewPO> {
}

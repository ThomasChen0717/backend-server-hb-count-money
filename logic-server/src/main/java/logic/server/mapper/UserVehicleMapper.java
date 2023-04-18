package logic.server.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import logic.server.po.UserVehiclePO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserVehicleMapper extends BaseMapper<UserVehiclePO> {
}

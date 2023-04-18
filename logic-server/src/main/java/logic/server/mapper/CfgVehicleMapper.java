package logic.server.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import logic.server.po.CfgVehiclePO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CfgVehicleMapper extends BaseMapper<CfgVehiclePO> {
}

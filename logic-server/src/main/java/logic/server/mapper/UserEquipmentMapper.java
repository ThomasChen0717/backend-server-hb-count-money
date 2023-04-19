package logic.server.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import logic.server.po.UserEquipmentPO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserEquipmentMapper extends BaseMapper<UserEquipmentPO> {
}

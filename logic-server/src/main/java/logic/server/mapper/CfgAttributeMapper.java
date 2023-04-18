package logic.server.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import logic.server.po.CfgAttributePO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CfgAttributeMapper extends BaseMapper<CfgAttributePO> {
}

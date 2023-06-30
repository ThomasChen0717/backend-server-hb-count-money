package logic.server.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import logic.server.po.LogCollectPO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface LogCollectMapper extends BaseMapper<LogCollectPO> {
}

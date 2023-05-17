package guide.server.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import guide.server.po.ClientVersionPO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ClientVersionMapper extends BaseMapper<ClientVersionPO> {
}

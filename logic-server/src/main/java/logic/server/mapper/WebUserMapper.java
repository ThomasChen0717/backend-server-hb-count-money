package logic.server.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import org.apache.ibatis.annotations.Mapper;
import logic.server.po.WebUserPO;

@Mapper
public interface WebUserMapper extends BaseMapper<WebUserPO> {
}
package admin.server.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import org.apache.ibatis.annotations.Mapper;
import admin.server.po.WebRouterPO;

@Mapper
public interface WebRouterMapper extends BaseMapper<WebRouterPO> {
}
package logic.server.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import logic.server.po.UserBossPO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserBossMapper extends BaseMapper<UserBossPO> {
}

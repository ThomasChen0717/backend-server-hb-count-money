package logic.server.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import logic.server.po.UserPO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

/**
 * @author mark
 * @date 2023-04-12
 */
@Mapper
public interface UserMapper extends BaseMapper<UserPO> {
    @Update("update t_user set `online_server_id` = 0 where `online_server_id` = #{onlineServerId}")
    Integer updateOnlineServerId(@Param("onlineServerId") int onlineServerId);

    @Update("update t_user set `online_server_id` = #{onlineServerId} where `id` = #{userId}")
    Integer updateOnlineServerIdById(@Param("userId") long userId,@Param("onlineServerId") int onlineServerId);

    @Update("update t_user set `is_online` = #{isOnline} where `id` = #{userId}")
    Integer updateIsOnlineById(@Param("userId") long userId,@Param("isOnline") boolean isOnline);
}

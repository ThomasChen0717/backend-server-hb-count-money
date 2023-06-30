package admin.server.repository;

import admin.server.dto.UserCountDTO;
import admin.server.dto.WebRouterDTO;
import admin.server.mapper.UserCountMapper;
import admin.server.po.UserCountPO;
import admin.server.po.WebRouterPO;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Repository
@AllArgsConstructor
public class UserCountRepositoryImpl implements UserCountRepository{
    UserCountMapper userCountMapper;
    @Override
    public Map<Long, UserCountDTO> getMap() {
        List<UserCountPO> userCountPOList = userCountMapper.selectList(new QueryWrapper<UserCountPO>()
                .lambda());
        List<UserCountDTO> userCountDTOListDTOList = Convertor.convert(userCountPOList, UserCountDTO.class);
        Map<Long, UserCountDTO> userCountDTOMap = userCountDTOListDTOList.stream().collect(Collectors.toMap(UserCountDTO::getId, UserCountDTO -> UserCountDTO));
        return userCountDTOMap;
    }
}

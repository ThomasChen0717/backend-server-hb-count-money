package logic.server.repository;

import logic.server.dto.UserCountDTO;
import logic.server.mapper.UserCountMapper;
import logic.server.po.UserCountPO;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
@AllArgsConstructor
public class UserCountRepositoryImpl implements UserCountRepository{
    private final UserCountMapper userCountMapper;
    @Override
    public int add(UserCountDTO dto) {
        UserCountPO po = Convertor.convert(UserCountPO.class, dto);
        int result = userCountMapper.insert(po);
        return result;
    }
}

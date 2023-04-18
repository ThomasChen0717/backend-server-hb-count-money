package logic.server.repository;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import logic.server.dto.UserVehicleDTO;
import logic.server.mapper.UserVehicleMapper;
import logic.server.po.UserVehiclePO;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
@AllArgsConstructor
public class UserVehicleRepositoryImpl implements UserVehicleRepository {
    private final UserVehicleMapper userVehicleMapper;

    @Override
    public int add(UserVehicleDTO dto) {
        UserVehiclePO po = Convertor.convert(UserVehiclePO.class, dto);
        return userVehicleMapper.insert(po);
    }

    @Override
    public int update(UserVehicleDTO dto){
        UserVehiclePO po = Convertor.convert(UserVehiclePO.class, dto);
        return userVehicleMapper.update(po, new QueryWrapper<UserVehiclePO>()
                .lambda().eq(UserVehiclePO::getUserId, po.getUserId()));
    }

    @Override
    public UserVehicleDTO get(long userId){
        UserVehiclePO po = userVehicleMapper.selectOne(new QueryWrapper<UserVehiclePO>()
                .lambda().eq(UserVehiclePO::getUserId, userId));
        return Convertor.convert(UserVehicleDTO.class, po);
    }
}

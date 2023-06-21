package logic.server.repository;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import logic.server.dto.UserVehicleDTO;
import logic.server.mapper.UserVehicleMapper;
import logic.server.po.UserMagnatePO;
import logic.server.po.UserVehiclePO;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Repository
@AllArgsConstructor
public class UserVehicleRepositoryImpl implements UserVehicleRepository {
    private final UserVehicleMapper userVehicleMapper;

    @Override
    public int add(UserVehicleDTO dto) {
        UserVehiclePO po = Convertor.convert(UserVehiclePO.class, dto);
        int result = userVehicleMapper.insert(po);
        dto.setId(po.getId());
        return result;
    }

    @Override
    public int update(UserVehicleDTO dto){
        UserVehiclePO po = Convertor.convert(UserVehiclePO.class, dto);
        return userVehicleMapper.update(po, new QueryWrapper<UserVehiclePO>()
                .lambda().eq(UserVehiclePO::getId, po.getId()));
    }

    @Override
    public Map<Integer,UserVehicleDTO> getMap(long userId){
        List<UserVehiclePO> cfgVehiclePOList = userVehicleMapper.selectList(new QueryWrapper<UserVehiclePO>()
                .lambda()
                .eq(UserVehiclePO::getUserId,userId)
        );
        List<UserVehicleDTO> vehicleDTOList = Convertor.convert(cfgVehiclePOList, UserVehicleDTO.class);
        Map<Integer, UserVehicleDTO> cfgVehicleDTOmap = vehicleDTOList.stream().collect(Collectors.toMap(UserVehicleDTO::getVehicleId, UserVehicleDTO -> UserVehicleDTO));
        return cfgVehicleDTOmap;
    }

    @Override
    public void deleteByUserId(long userId){
        userVehicleMapper.delete(new QueryWrapper<UserVehiclePO>()
                .lambda()
                .eq(UserVehiclePO::getUserId, userId)
        );
    }
}

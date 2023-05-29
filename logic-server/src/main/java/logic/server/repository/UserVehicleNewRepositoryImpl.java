package logic.server.repository;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import logic.server.dto.UserVehicleNewDTO;
import logic.server.mapper.UserVehicleNewMapper;
import logic.server.po.UserVehicleNewPO;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Repository
@AllArgsConstructor
public class UserVehicleNewRepositoryImpl implements UserVehicleNewRepository{
    private final UserVehicleNewMapper userVehicleNewMapper;

    @Override
    public int add(UserVehicleNewDTO dto) {
        UserVehicleNewPO po = Convertor.convert(UserVehicleNewPO.class, dto);
        int result = userVehicleNewMapper.insert(po);
        dto.setId(po.getId());
        return result;
    }

    @Override
    public int update(UserVehicleNewDTO dto){
        UserVehicleNewPO po = Convertor.convert(UserVehicleNewPO.class, dto);
        return userVehicleNewMapper.update(po, new QueryWrapper<UserVehicleNewPO>()
                .lambda().eq(UserVehicleNewPO::getId, po.getId()));
    }

    @Override
    public Map<Integer, UserVehicleNewDTO> getMap(long userId){
        List<UserVehicleNewPO> cfgVehicleNewPOList = userVehicleNewMapper.selectList(new QueryWrapper<UserVehicleNewPO>()
                .lambda()
                .eq(UserVehicleNewPO::getUserId,userId)
        );
        List<UserVehicleNewDTO> vehicleNewDTOList = Convertor.convert(cfgVehicleNewPOList, UserVehicleNewDTO.class);
        Map<Integer, UserVehicleNewDTO> cfgVehicleNewDTOmap = vehicleNewDTOList.stream().collect(Collectors.toMap(UserVehicleNewDTO::getVehicleId, UserVehicleNewDTO -> UserVehicleNewDTO));
        return cfgVehicleNewDTOmap;
    }
}

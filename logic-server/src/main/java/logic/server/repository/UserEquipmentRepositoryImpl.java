package logic.server.repository;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import logic.server.dto.UserEquipmentDTO;
import logic.server.dto.UserVehicleDTO;
import logic.server.mapper.UserEquipmentMapper;
import logic.server.po.UserBossPO;
import logic.server.po.UserEquipmentPO;
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
public class UserEquipmentRepositoryImpl implements UserEquipmentRepository{
    private final UserEquipmentMapper userEquipmentMapper;

    @Override
    public int add(UserEquipmentDTO dto) {
        UserEquipmentPO po = Convertor.convert(UserEquipmentPO.class, dto);
        int result = userEquipmentMapper.insert(po);
        dto.setId(po.getId());
        return result;
    }

    @Override
    public int update(UserEquipmentDTO dto){
        UserEquipmentPO po = Convertor.convert(UserEquipmentPO.class, dto);
        return userEquipmentMapper.update(po, new QueryWrapper<UserEquipmentPO>()
                .lambda().eq(UserEquipmentPO::getId, po.getId()));
    }

    @Override
    public Map<Integer, UserEquipmentDTO> getMap(long userId){
        List<UserEquipmentPO> cfgEquipmentPOList = userEquipmentMapper.selectList(new QueryWrapper<UserEquipmentPO>()
                .lambda()
                .eq(UserEquipmentPO::getUserId,userId)
        );
        List<UserEquipmentDTO> equipmentDTOList = Convertor.convert(cfgEquipmentPOList, UserEquipmentDTO.class);
        Map<Integer, UserEquipmentDTO> cfgEquipmentDTOmap = equipmentDTOList.stream().collect(Collectors.toMap(UserEquipmentDTO::getEquipmentId, UserEquipmentDTO -> UserEquipmentDTO));
        return cfgEquipmentDTOmap;
    }

    @Override
    public void deleteByUserId(long userId){
        userEquipmentMapper.delete(new QueryWrapper<UserEquipmentPO>()
                .lambda()
                .eq(UserEquipmentPO::getUserId, userId)
        );
    }
}

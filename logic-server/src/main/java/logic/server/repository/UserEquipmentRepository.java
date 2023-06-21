package logic.server.repository;

import logic.server.dto.UserEquipmentDTO;
import java.util.Map;

public interface UserEquipmentRepository {
    int add(UserEquipmentDTO dto);
    int update(UserEquipmentDTO dto);
    Map<Integer, UserEquipmentDTO> getMap(long userId);
    void deleteByUserId(long userId);
}

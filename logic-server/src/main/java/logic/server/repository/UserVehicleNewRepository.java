package logic.server.repository;

import logic.server.dto.UserVehicleNewDTO;

import java.util.Map;

public interface UserVehicleNewRepository {
    int add(UserVehicleNewDTO dto);
    int update(UserVehicleNewDTO dto);
    Map<Integer, UserVehicleNewDTO> getMap(long userId);
    void deleteByUserId(long userId);
}

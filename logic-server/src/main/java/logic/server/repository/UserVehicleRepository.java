package logic.server.repository;

import logic.server.dto.UserVehicleDTO;

import java.util.Map;

public interface UserVehicleRepository {
    int add(UserVehicleDTO dto);
    int update(UserVehicleDTO dto);
    Map<Integer,UserVehicleDTO> getMap(long userId);
    void deleteByUserId(long userId);
}

package logic.server.repository;

import logic.server.dto.UserVehicleDTO;

public interface UserVehicleRepository {
    int add(UserVehicleDTO dto);
    int update(UserVehicleDTO dto);
    UserVehicleDTO get(long userId);
}

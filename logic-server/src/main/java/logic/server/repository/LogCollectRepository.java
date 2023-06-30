package logic.server.repository;


import logic.server.dto.LogCollectDTO;

public interface LogCollectRepository {
    int add(LogCollectDTO dto);
}

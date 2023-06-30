package logic.server.repository;

import logic.server.dto.LogCollectDTO;
import logic.server.mapper.LogCollectMapper;
import logic.server.po.LogCollectPO;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
@AllArgsConstructor
public class LogCollectRepositoryImpl implements LogCollectRepository{
    private final LogCollectMapper logCollectMapper;
    @Override
    public int add(LogCollectDTO dto) {
        LogCollectPO po = Convertor.convert(LogCollectPO.class, dto);
        int result = logCollectMapper.insert(po);
        return result;
    }
}

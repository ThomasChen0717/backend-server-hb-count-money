package logic.server.repository;

import logic.server.dto.CfgAttributeDTO;

import java.util.Map;

public interface CfgAttributeRepository {
    Map<Integer, CfgAttributeDTO> getMap();

    int delete();

    int add(CfgAttributeDTO dto);
}

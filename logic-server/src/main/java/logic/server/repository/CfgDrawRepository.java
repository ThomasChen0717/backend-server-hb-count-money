package logic.server.repository;

import logic.server.dto.CfgDrawDTO;

import java.util.Map;

public interface CfgDrawRepository {
    Map<Integer, CfgDrawDTO> getMap();
}

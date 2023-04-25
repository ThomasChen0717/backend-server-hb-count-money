package logic.server.repository;

import logic.server.dto.CfgMagnateDTO;

import java.util.Map;

public interface CfgMagnateRepository {
    Map<Integer, CfgMagnateDTO> getMap();
}

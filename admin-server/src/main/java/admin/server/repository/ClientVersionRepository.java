package admin.server.repository;

import admin.server.dto.ClientVersionDTO;

import java.util.Map;

public interface ClientVersionRepository {

    Map<Long, ClientVersionDTO> getMap();

    int add(ClientVersionDTO dto);

    int update(ClientVersionDTO dto);

    int deleteById(long id);
}

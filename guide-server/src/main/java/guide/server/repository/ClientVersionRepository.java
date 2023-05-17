package guide.server.repository;


import guide.server.dto.ClientVersionDTO;

public interface ClientVersionRepository {
    ClientVersionDTO get(int version);
}

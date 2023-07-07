package admin.server.service;

import admin.server.dto.ClientVersionDTO;
import admin.server.dto.UserCountDTO;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

public interface IWebDatabaseService {

    List<UserCountDTO> getUserCount(LocalDateTime date, String hour);

    List<UserCountDTO> getUserCountGraph(LocalDateTime date, String hour, int logicServerId);

    List<ClientVersionDTO> getClientVersion();

    void addNewClientVersion(ClientVersionDTO clientVersionDTO);

    void updateClientVersion(ClientVersionDTO clientVersionDTO);

    void deleteClientVersion(long id);
}

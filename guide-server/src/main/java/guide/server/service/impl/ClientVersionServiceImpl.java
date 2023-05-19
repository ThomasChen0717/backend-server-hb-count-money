package guide.server.service.impl;

import guide.server.dto.ClientVersionDTO;
import guide.server.repository.ClientVersionRepository;
import guide.server.service.IClientVersionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ClientVersionServiceImpl implements IClientVersionService {
    @Autowired
    private ClientVersionRepository clientVersionRepository;

    @Override
    public String getServerUrl(String clientVersion){
        log.info("ClientVersionServiceImpl::getServerUrl:clientVersion = {}",clientVersion);

        String serverUrl = "";

        ClientVersionDTO clientVersionDTO = clientVersionRepository.get(clientVersion);
        if(clientVersionDTO != null){
            serverUrl = clientVersionDTO.isVerified() == true ? clientVersionDTO.getReleaseServer() : clientVersionDTO.getVerificationServer();
        }

        log.info("ClientVersionServiceImpl::getServerUrl:serverUrl = {},clientVersionDTO = {}",serverUrl,clientVersionDTO);
        return serverUrl;
    }
}

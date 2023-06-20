package guide.server.service.impl;

import com.alibaba.fastjson.JSONObject;
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

    @Override
    public JSONObject getServerUrlNew(String clientVersion){
        log.info("ClientVersionServiceImpl::getServerUrlNew:clientVersion = {}",clientVersion);

        JSONObject jsonServerUrl = new JSONObject();

        ClientVersionDTO clientVersionDTO = clientVersionRepository.get(clientVersion);
        if(clientVersionDTO != null){
            String externalServer = clientVersionDTO.isVerified() == true ? clientVersionDTO.getReleaseServer() : clientVersionDTO.getVerificationServer();
            String preLoginServer = clientVersionDTO.isVerified() == true ? clientVersionDTO.getReleasePreLoginServer() : clientVersionDTO.getVerificationPreLoginServer();
            jsonServerUrl.put("externalServer",externalServer);
            jsonServerUrl.put("preLoginServer",preLoginServer);
        }

        log.info("ClientVersionServiceImpl::getServerUrlNew:jsonServerUrl = {},clientVersionDTO = {}",jsonServerUrl,clientVersionDTO);
        return jsonServerUrl;
    }
}

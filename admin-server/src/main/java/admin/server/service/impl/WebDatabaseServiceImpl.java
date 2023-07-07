package admin.server.service.impl;


import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import admin.server.dto.*;
import admin.server.repository.*;
import admin.server.service.IWebDatabaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;


@Slf4j
@Service
public class WebDatabaseServiceImpl implements IWebDatabaseService {

    @Autowired
    private UserCountRepository userCountRepository;

    @Autowired
    private ClientVersionRepository clientVersionRepository;


    @Override
    public List<UserCountDTO> getUserCount(LocalDateTime date, String hour){
        Map<Long, UserCountDTO> userCountDTOMap = userCountRepository.getMap();
        List<UserCountDTO> resList = new ArrayList<>();
        for(long key: userCountDTOMap.keySet()){
            UserCountDTO userCountDTO = userCountDTOMap.get(key);
            LocalDateTime createTime = userCountDTO.getCreateTime();
            if(createTime.getYear() == date.getYear() && createTime.getDayOfYear() == date.getDayOfYear()) {
                if(hour == null){
                    userCountDTO.setCreateTimeString(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(userCountDTO.getCreateTime()));
                    resList.add(userCountDTO);
                }
                else{
                    if(createTime.getHour() == Integer.parseInt(hour)){
                        userCountDTO.setCreateTimeString(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(userCountDTO.getCreateTime()));
                        resList.add(userCountDTO);
                    }
                }
            }
        }
        return resList;
    }
    @Override
    public List<UserCountDTO> getUserCountGraph(LocalDateTime date, String hour, int logicServerId){
        Map<Long, UserCountDTO> userCountDTOMap = userCountRepository.getMap();
        List<UserCountDTO> resList = new ArrayList<>();
        for(long key: userCountDTOMap.keySet()) {
            UserCountDTO userCountDTO = userCountDTOMap.get(key);
            LocalDateTime createTime = userCountDTO.getCreateTime();
            if (createTime.getYear() == date.getYear() && createTime.getDayOfYear() == date.getDayOfYear() && userCountDTOMap.get(key).getLogicServerId() == logicServerId && createTime.getHour() == Integer.parseInt(hour)) {
                userCountDTO.setCreateTimeString(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(userCountDTO.getCreateTime()));
                resList.add(userCountDTO);
            }
        }
        return resList;
    }

    @Override
    public List<ClientVersionDTO> getClientVersion() {
        Map<Long, ClientVersionDTO> clientVersionDTOMap = clientVersionRepository.getMap();
        List<ClientVersionDTO> resList = new ArrayList<>();
        for(Long key: clientVersionDTOMap.keySet()){
            ClientVersionDTO clientVersionDTO = clientVersionDTOMap.get(key);
            resList.add(clientVersionDTO);
        }
        return resList;
    }

    @Override
    public void addNewClientVersion(ClientVersionDTO dto){
        clientVersionRepository.add(dto);
    }

    @Override
    public void updateClientVersion(ClientVersionDTO dto) {
        clientVersionRepository.update(dto);
    }

    @Override
    public void deleteClientVersion(long id) {
        clientVersionRepository.deleteById(id);
    }


}

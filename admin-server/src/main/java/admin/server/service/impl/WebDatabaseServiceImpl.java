package admin.server.service.impl;


import admin.server.entity.UserActivityCount;
import lombok.extern.slf4j.Slf4j;
import admin.server.dto.*;
import admin.server.repository.*;
import admin.server.service.IWebDatabaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;


@Slf4j
@Service
public class WebDatabaseServiceImpl implements IWebDatabaseService {

    @Autowired
    private UserCountRepository userCountRepository;

    @Autowired
    private ClientVersionRepository clientVersionRepository;

    @Autowired UserRepository userRepository;

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

    @Override
    public UserActivityCount getUserActivity(List<LocalDateTime> dateRange) {
        Map<Long, UserDTO> userDTOMap = userRepository.getMap();
        dateRange.set(0, dateRange.get(0).plusHours(8));
        dateRange.set(1,dateRange.get(1).plusHours(8));
        UserActivityCount res = new UserActivityCount();
        int activeUserCount = 0;
        int newUserCount = 0;
        int totalUserCount = 0;
        for(Long key: userDTOMap.keySet()){
            UserDTO userDTO = userDTOMap.get(key);
            LocalDateTime createTime = userDTO.getCreateTime();
            LocalDateTime updateTime = userDTO.getUpdateTime();
            if(createTime.compareTo(dateRange.get(0)) >= 0 && createTime.compareTo(dateRange.get(1)) <= 0) {
                newUserCount++;
            }
            if(updateTime.compareTo(dateRange.get(0)) >= 0 && createTime.compareTo(dateRange.get(1)) <= 0){
                activeUserCount++;
            }
            if(createTime.compareTo(dateRange.get(1)) <= 0) {
                totalUserCount++;
            }
        }
        res.setActiveUserCount(activeUserCount);
        res.setNewUserCount(newUserCount);
        res.setTotalUserCount(totalUserCount);
        return res;
    }

    @Override
    public List<List<UserActivityCount>> getUserActivityTable(List<LocalDateTime> dateRange) {
        Map<Long, UserDTO> userDTOMap = userRepository.getMap();
        dateRange.set(0, dateRange.get(0).plusHours(8));
        dateRange.set(1, dateRange.get(1).plusHours(8));
        LocalDateTime startDate = dateRange.get(0);
        LocalDateTime endDate = dateRange.get(1);

        int totalDays = (int) ChronoUnit.DAYS.between(startDate.toLocalDate(), endDate.toLocalDate()) + 1;

        List<List<UserActivityCount>> activityByHour = new ArrayList<>();

        for (int day = 0; day < totalDays; day++) {
            List<UserActivityCount> activityPerHour = new ArrayList<>();

            for (int hour = 0; hour < 24; hour++) {
                LocalDateTime currentDateTime = startDate.plusDays(day).plusHours(hour);
                String time = DateTimeFormatter.ofPattern("HH:mm:ss").format(currentDateTime);
                UserActivityCount res = new UserActivityCount();
                int activeUserCount = 0;
                int newUserCount = 0;
                int totalUserCount = 0;

                for (Long key : userDTOMap.keySet()) {
                    UserDTO userDTO = userDTOMap.get(key);
                    LocalDateTime createTime = userDTO.getCreateTime();
                    LocalDateTime updateTime = userDTO.getUpdateTime();

                    if (createTime.compareTo(currentDateTime) >= 0 && createTime.compareTo(currentDateTime.plusHours(1)) < 0) {
                        newUserCount++;
                    }

                    if (updateTime.compareTo(currentDateTime) >= 0 && updateTime.compareTo(currentDateTime.plusHours(1)) < 0) {
                        activeUserCount++;
                    }
                    if(createTime.compareTo(currentDateTime.plusHours(1)) < 0) {
                        totalUserCount++;
                    }
                }

                res.setActiveUserCount(activeUserCount);
                res.setNewUserCount(newUserCount);
                res.setTotalUserCount(totalUserCount);
                res.setTime(time);

                activityPerHour.add(res);
            }

            activityByHour.add(activityPerHour);
        }

        return activityByHour;
    }


}

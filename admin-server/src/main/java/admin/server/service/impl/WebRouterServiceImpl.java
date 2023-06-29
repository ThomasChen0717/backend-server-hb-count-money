package admin.server.service.impl;


import admin.server.entity.Role;
import admin.server.entity.RouteData;
import admin.server.repository.WebRouterRepository;
import admin.server.service.WebRouterService;
import admin.server.dto.WebRouterDTO;

import static admin.server.util.rolesUtil.convertListToString;
import static admin.server.util.rolesUtil.convertRoles;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class WebRouterServiceImpl implements WebRouterService {
    @Autowired
    WebRouterRepository webRouterRepository;

    @Override
    public List<String> getRoles(WebRouterDTO dto) {
        String route = dto.getRouteName();
        Map<String, WebRouterDTO> webRouterDTOMap = webRouterRepository.getMap();
        String roles = webRouterDTOMap.get(route).getRoles();
        return convertRoles(roles);
    }

    @Override
    public void deleteRole(String role) {
        Map<String, WebRouterDTO> webRouterDTOMap = webRouterRepository.getMap();
        role = "\'" + role + "\'";
        for (String key : webRouterDTOMap.keySet()) {
            String roles = webRouterDTOMap.get(key).getRoles();
            List<String> roleList = convertRoles(roles);
            if (roleList.contains(role)) {
                roleList.remove(role);
                String res = convertListToString(roleList);
                WebRouterDTO dto = webRouterDTOMap.get(key).setRoles(res);
                webRouterRepository.update(dto);
            }
        }
    }

    @Override
    public boolean addRole(Role role){
        Map<String, WebRouterDTO> webRouterDTOMap = webRouterRepository.getMap();

        List<RouteData> routes = role.getRoutes();
        role.setName("\'" + role.getName() + "\'");

        String allRoles = webRouterDTOMap.get("AllRoles").getRoles();
        List<String> allRoleList = convertRoles(allRoles);
        if(!allRoleList.contains(role.getName())) {
            allRoleList.add(role.getName());
            String res = convertListToString(allRoleList);
            WebRouterDTO dto = webRouterDTOMap.get("AllRoles").setRoles(res);
            webRouterRepository.update(dto);
        }
        else{
            return false;
        }

        for (int i = 0; i < routes.size(); i++) {
            String routeName = routes.get(i).getName();
            if (webRouterDTOMap.containsKey(routeName)) {
                String roles = webRouterDTOMap.get(routeName).getRoles();
                List<String> roleList = convertRoles(roles);
                roleList.add(role.getName());

            }
        }

        return true;
    }

    @Override
    public void updateRole(Role role) {
        Map<String, WebRouterDTO> webRouterDTOMap = webRouterRepository.getMap();
        List<RouteData> routes = role.getRoutes();
        role.setName("\'" + role.getName() + "\'");
        for (String key : webRouterDTOMap.keySet()) {
            boolean hasRoute = false;
            if(key.equals("AllRoles")){
                hasRoute = true;
                continue;
            }
            for (int i = 0; i < routes.size(); i++) {
                String routeName = routes.get(i).getName();
                if (routeName.equals(key)){
                    hasRoute = true;
                    String roles = webRouterDTOMap.get(key).getRoles();
                    List<String> roleList = convertRoles(roles);
                    if (!roleList.contains(role.getName())) {
                        roleList.add(role.getName());
                        String res = convertListToString(roleList);
                        WebRouterDTO dto = webRouterDTOMap.get(key).setRoles(res);
                        webRouterRepository.update(dto);
                    }
                    break;
                }
            }
            if(!hasRoute){
                String roles = webRouterDTOMap.get(key).getRoles();
                List<String> roleList = convertRoles(roles);
                if(roleList.contains(role.getName())) {
                    roleList.remove(role.getName());
                    String res = convertListToString(roleList);
                    WebRouterDTO dto = webRouterDTOMap.get(key).setRoles(res);
                    webRouterRepository.update(dto);
                }
            }
        }
    }
}

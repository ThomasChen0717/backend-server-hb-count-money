package admin.server.service.impl;


import admin.server.dto.RoleDTO;
import admin.server.entity.RouteData;
import admin.server.repository.WebRouterRepository;
import admin.server.service.IWebRouterService;
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
public class WebRouterServiceImpl implements IWebRouterService {
    @Autowired
    private WebRouterRepository webRouterRepository;

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
                WebRouterDTO webUserDTO = webRouterDTOMap.get(key).setRoles(res);
                webRouterRepository.update(webUserDTO);
            }
        }
    }

    @Override
    public boolean addRole(RoleDTO roleDTO){
        Map<String, WebRouterDTO> webRouterDTOMap = webRouterRepository.getMap();

        List<RouteData> routes = roleDTO.getRoutes();
        roleDTO.setName("\'" + roleDTO.getName() + "\'");

        String allRoles = webRouterDTOMap.get("AllRoles").getRoles();
        List<String> allRoleList = convertRoles(allRoles);
        if(!allRoleList.contains(roleDTO.getName())) {
            allRoleList.add(roleDTO.getName());
            String res = convertListToString(allRoleList);
            WebRouterDTO webUserDTO = webRouterDTOMap.get("AllRoles").setRoles(res);
            webRouterRepository.update(webUserDTO);
        }
        else{
            return false;
        }

        for (int i = 0; i < routes.size(); i++) {
            String routeName = routes.get(i).getName();
            if (webRouterDTOMap.containsKey(routeName)) {
                String roles = webRouterDTOMap.get(routeName).getRoles();
                List<String> roleList = convertRoles(roles);
                roleList.add(roleDTO.getName());
                String res = convertListToString(roleList);
                WebRouterDTO webUserDTO = webRouterDTOMap.get(routeName).setRoles(res);
                webRouterRepository.update(webUserDTO);
            }
        }
        return true;
    }

    @Override
    public void updateRole(RoleDTO roleDTO) {
        Map<String, WebRouterDTO> webRouterDTOMap = webRouterRepository.getMap();
        List<RouteData> routes = roleDTO.getRoutes();
        roleDTO.setName("\'" + roleDTO.getName() + "\'");
        for (String key : webRouterDTOMap.keySet()) {
            boolean hasRoute = false;
            if(key.equals("AllRoles")){
                continue;
            }
            for (int i = 0; i < routes.size(); i++) {
                String routeName = routes.get(i).getName();
                if (routeName.equals(key)){
                    hasRoute = true;
                    String roles = webRouterDTOMap.get(key).getRoles();
                    List<String> roleList = convertRoles(roles);
                    if (!roleList.contains(roleDTO.getName())) {
                        roleList.add(roleDTO.getName());
                        String res = convertListToString(roleList);
                        WebRouterDTO webUserDTO = webRouterDTOMap.get(key).setRoles(res);
                        webRouterRepository.update(webUserDTO);
                    }
                    break;
                }
            }
            if(!hasRoute){
                String roles = webRouterDTOMap.get(key).getRoles();
                List<String> roleList = convertRoles(roles);
                if(roleList.contains(roleDTO.getName())) {
                    roleList.remove(roleDTO.getName());
                    String res = convertListToString(roleList);
                    WebRouterDTO webUserDTO = webRouterDTOMap.get(key).setRoles(res);
                    webRouterRepository.update(webUserDTO);
                }
            }
        }
    }
}

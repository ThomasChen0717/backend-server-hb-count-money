package admin.server.service;

import admin.server.dto.WebRouterDTO;
import admin.server.dto.RoleDTO;

import java.util.List;

public interface IWebRouterService {
    List<String> getRoles(WebRouterDTO dto);

    void deleteRole(String role);

    boolean addRole(RoleDTO roleDTO);

    void updateRole(RoleDTO roleDTO, String oldName);
}

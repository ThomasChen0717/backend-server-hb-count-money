package admin.server.service;

import admin.server.dto.WebRouterDTO;
import admin.server.entity.Role;

import java.util.List;

public interface WebRouterService {
    List<String> getRoles(WebRouterDTO dto);

    void deleteRole(String role);

    boolean addRole(Role role);

    void updateRole(Role role);
}

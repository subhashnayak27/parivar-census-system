package com.parivar.census.role.service;

import com.parivar.census.role.dto.request.RoleRequest;
import com.parivar.census.role.dto.response.RoleResponse;

import java.util.List;
public interface RoleService {

    RoleResponse createRole(RoleRequest request);

    RoleResponse updateRole(Long id, RoleRequest request);

    RoleResponse getRoleById(Long id);

    List<RoleResponse> getAllRoles();

    void deleteRole(Long id);

}
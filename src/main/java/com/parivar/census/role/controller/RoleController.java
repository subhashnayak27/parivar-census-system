package com.parivar.census.role.controller;

import com.parivar.census.common.ApiResponse;
import com.parivar.census.role.dto.request.RoleRequest;
import com.parivar.census.role.dto.response.RoleResponse;
import com.parivar.census.role.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;
import java.util.List;

@RestController
@RequestMapping("/api/roles")
@RequiredArgsConstructor
@PreAuthorize("hasRole('SUPER_ADMIN')")
public class RoleController {

    private final RoleService roleService;

    @PostMapping
    public ApiResponse<RoleResponse> createRole(
            @RequestBody RoleRequest request) {

        return ApiResponse.success(
                "Role created successfully",
                roleService.createRole(request));
    }

    @PutMapping("/{id}")
    public ApiResponse<RoleResponse> updateRole(
            @PathVariable Long id,
            @RequestBody RoleRequest request) {

        return ApiResponse.success(
                "Role updated successfully",
                roleService.updateRole(id, request));
    }

    @GetMapping("/{id}")
    public ApiResponse<RoleResponse> getRoleById(
            @PathVariable Long id) {

        return ApiResponse.success(
                roleService.getRoleById(id));
    }

    @GetMapping
    public ApiResponse<List<RoleResponse>> getAllRoles() {

        return ApiResponse.success(
                roleService.getAllRoles());
    }

    @DeleteMapping("/{id}")
    public ApiResponse<String> deleteRole(
            @PathVariable Long id) {

        roleService.deleteRole(id);

        return ApiResponse.success(
                "Role deleted successfully",
                "OK");
    }
}
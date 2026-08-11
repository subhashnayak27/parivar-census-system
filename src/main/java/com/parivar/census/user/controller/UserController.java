package com.parivar.census.user.controller;

import com.parivar.census.common.ApiResponse;
import com.parivar.census.common.dto.PageResponse;
import com.parivar.census.common.dto.PaginationRequest;
import com.parivar.census.user.dto.request.UpdateUserRoleRequest;
import com.parivar.census.user.dto.request.UserRequest;
import com.parivar.census.user.dto.response.UserResponse;
import com.parivar.census.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    @PreAuthorize("hasAnyRole('SUPER_ADMIN')")
    public ApiResponse<UserResponse> createUser(
            @RequestBody UserRequest request) {

        return ApiResponse.success(
                "User created successfully",
                userService.createUser(request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN')")
    public ApiResponse<UserResponse> updateUser(
            @PathVariable Long id,
            @RequestBody UserRequest request) {

        return ApiResponse.success(
                "User updated successfully",
                userService.updateUser(id, request));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','ADMIN')")
    public ApiResponse<UserResponse> getUserById(
            @PathVariable Long id) {

        return ApiResponse.success(
                userService.getUserById(id));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','ADMIN')")
    public ApiResponse<PageResponse<UserResponse>> getAllUsers(
            PaginationRequest request) {

        return ApiResponse.success(
                "Users fetched successfully",
                userService.getAllUsers(request));
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','ADMIN')")
    public ApiResponse<UserResponse> updateUserStatus(
            @PathVariable Long id,
            @RequestParam Boolean active) {

        return ApiResponse.success(
                "User status updated successfully",
                userService.updateUserStatus(id, active));
    }

    @PatchMapping("/{id}/role")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN')")
    public ApiResponse<UserResponse> updateUserRole(
            @PathVariable Long id,
            @Valid @RequestBody UpdateUserRoleRequest request) {

        return ApiResponse.success(
                "User role updated successfully",
                userService.updateUserRole(
                        id,
                        request.getRoleId()));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN')")
    public ApiResponse<String> deleteUser(
            @PathVariable Long id) {

        userService.deleteUser(id);

        return ApiResponse.success(
                "User deleted successfully",
                "OK");
    }

}
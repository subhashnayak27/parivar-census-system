package com.parivar.census.user.service;

import com.parivar.census.common.dto.PageResponse;
import com.parivar.census.common.dto.PaginationRequest;
import com.parivar.census.user.dto.request.UserRequest;
import com.parivar.census.user.dto.response.UserResponse;

public interface UserService {
    UserResponse createUser(UserRequest request);
    UserResponse updateUser(Long id, UserRequest request);
    UserResponse getUserById(Long id);
    PageResponse<UserResponse> getAllUsers(PaginationRequest request);
    UserResponse updateUserStatus(Long id, Boolean active);
    UserResponse updateUserRole(Long id, Long roleId);
    void deleteUser(Long id);
}
package com.parivar.census.auth.service;

import com.parivar.census.auth.dto.request.LoginRequest;
import com.parivar.census.auth.dto.request.RegisterRequest;
import com.parivar.census.auth.dto.response.LoginResponse;
import com.parivar.census.role.repository.RoleRepository;
import com.parivar.census.user.dto.response.UserResponse;

public interface AuthService {


    LoginResponse login(LoginRequest request);
    UserResponse register(RegisterRequest request);

}
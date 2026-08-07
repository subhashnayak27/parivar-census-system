package com.parivar.census.auth.service;

import com.parivar.census.auth.dto.request.LoginRequest;
import com.parivar.census.auth.dto.response.LoginResponse;

public interface AuthService {

    LoginResponse login(LoginRequest request);

}
package com.parivar.census.auth.controller;

import com.parivar.census.auth.dto.request.LoginRequest;
import com.parivar.census.auth.dto.request.RegisterRequest;
import com.parivar.census.auth.dto.response.LoginResponse;
import com.parivar.census.auth.service.AuthService;
import com.parivar.census.common.ApiResponse;
import com.parivar.census.user.dto.response.UserResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ApiResponse<LoginResponse> login(
            @RequestBody LoginRequest request) {

        return ApiResponse.success(
                "Login successful",
                authService.login(request)
        );
    }

    @PostMapping("/register")
    public ApiResponse<UserResponse> register(
            @Valid @RequestBody RegisterRequest request) {

        return ApiResponse.success(
                "Registration successful",
                authService.register(request)
        );
    }
}
package com.parivar.census.auth.service.impl;

import com.parivar.census.auth.dto.request.LoginRequest;
import com.parivar.census.auth.dto.response.LoginResponse;
import com.parivar.census.auth.service.AuthService;
import com.parivar.census.exception.ResourceNotFoundException;
import com.parivar.census.security.jwt.JwtService;
import com.parivar.census.user.entity.User;
import com.parivar.census.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Override
    public LoginResponse login(LoginRequest request) {

        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Invalid username or password"));

        if (!Boolean.TRUE.equals(user.getActive())) {
            throw new ResourceNotFoundException(
                    "User account is inactive");
        }

        if (!passwordEncoder.matches(
                request.getPassword(),
                user.getPassword())) {

            throw new ResourceNotFoundException(
                    "Invalid username or password");
        }

        UserDetails userDetails =
                org.springframework.security.core.userdetails.User
                        .withUsername(user.getUsername())
                        .password(user.getPassword())
                        .authorities(
                                "ROLE_" + user.getRole().getRoleName().name())
                        .build();

        String token = jwtService.generateToken(userDetails);

        return LoginResponse.builder()
                .accessToken(token)
                .tokenType("Bearer")
                .username(user.getUsername())
                .fullName(user.getFullName())
                .role(user.getRole().getRoleName().name())
                .build();
    }
}
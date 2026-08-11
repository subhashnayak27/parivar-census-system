package com.parivar.census.auth.service.impl;

import com.parivar.census.auth.dto.request.LoginRequest;
import com.parivar.census.auth.dto.request.RegisterRequest;
import com.parivar.census.auth.dto.response.LoginResponse;
import com.parivar.census.auth.service.AuthService;
import com.parivar.census.exception.DuplicateResourceException;
import com.parivar.census.exception.ResourceNotFoundException;
import com.parivar.census.role.entity.Role;
import com.parivar.census.role.enums.RoleName;
import com.parivar.census.role.repository.RoleRepository;
import com.parivar.census.security.jwt.JwtService;
import com.parivar.census.user.dto.response.UserResponse;
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
    private final RoleRepository roleRepository;

    @Override
    public LoginResponse login(LoginRequest request) {

        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Invalid username or password"
                        ));

        if (!Boolean.TRUE.equals(user.getActive())) {
            throw new ResourceNotFoundException(
                    "User account is inactive"
            );
        }

        if (!passwordEncoder.matches(
                request.getPassword(),
                user.getPassword())) {

            throw new ResourceNotFoundException(
                    "Invalid username or password"
            );
        }

        UserDetails userDetails =
                org.springframework.security.core.userdetails.User
                        .withUsername(user.getUsername())
                        .password(user.getPassword())
                        .authorities(
                                "ROLE_" +
                                        user.getRole()
                                                .getRoleName()
                                                .name()
                        )
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

    @Override
    public UserResponse register(RegisterRequest request) {

        // ================================
        // Username validation
        // ================================

        if (userRepository.existsByUsername(
                request.getUsername())) {

            throw new DuplicateResourceException(
                    "Username already exists"
            );
        }

        // ================================
        // Email validation
        // ================================

        if (request.getEmail() != null
                && !request.getEmail().isBlank()
                && userRepository.existsByEmail(
                request.getEmail())) {

            throw new DuplicateResourceException(
                    "Email already exists"
            );
        }

        // ================================
        // Get VIEWER role
        // ================================

        Role viewerRole = roleRepository
                .findByRoleName(RoleName.VIEWER)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "VIEWER role not configured"
                        ));

        // ================================
        // Create user
        // ================================

        User user = User.builder()
                .username(request.getUsername())
                .password(
                        passwordEncoder.encode(
                                request.getPassword()
                        )
                )
                .fullName(request.getFullName())
                .email(request.getEmail())
                .mobileNo(request.getMobileNo())
                .role(viewerRole)
                .active(true)
                .build();

        User savedUser = userRepository.save(user);

        // ================================
        // Response
        // ================================

        return UserResponse.builder()
                .id(savedUser.getId())
                .username(savedUser.getUsername())
                .fullName(savedUser.getFullName())
                .email(savedUser.getEmail())
                .mobileNo(savedUser.getMobileNo())
                .roleName(savedUser.getRole().getRoleName().name())
                .active(savedUser.getActive())
                .build();
    }
}
package com.parivar.census.auth.service.impl;

import com.parivar.census.auth.dto.request.ForgotPasswordRequest;
import com.parivar.census.auth.dto.request.LoginRequest;
import com.parivar.census.auth.dto.request.RegisterRequest;
import com.parivar.census.auth.dto.request.ResetPasswordRequest;
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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private static final Logger logger =
            LoggerFactory.getLogger(AuthServiceImpl.class);

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtService jwtService;

    private final RoleRepository roleRepository;


    // ==========================================
    // LOGIN
    // ==========================================

    @Override
    public LoginResponse login(LoginRequest request) {

        logger.info(
                "Login attempt for username: {}",
                request.getUsername()
        );

        User user = userRepository
                .findByUsername(request.getUsername())
                .orElseThrow(() -> {

                    logger.warn(
                            "Login failed. User not found: {}",
                            request.getUsername()
                    );

                    return new ResourceNotFoundException(
                            "Invalid username or password"
                    );
                });


        if (!Boolean.TRUE.equals(user.getActive())) {

            logger.warn(
                    "Login failed. User account is inactive: {}",
                    request.getUsername()
            );

            throw new ResourceNotFoundException(
                    "User account is inactive"
            );
        }


        if (!passwordEncoder.matches(
                request.getPassword(),
                user.getPassword()
        )) {

            logger.warn(
                    "Login failed. Invalid password for username: {}",
                    request.getUsername()
            );

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


        String token =
                jwtService.generateToken(userDetails);


        logger.info(
                "Login successful for username: {}",
                user.getUsername()
        );


        return LoginResponse.builder()
                .accessToken(token)
                .tokenType("Bearer")
                .username(user.getUsername())
                .fullName(user.getFullName())
                .role(
                        user.getRole()
                                .getRoleName()
                                .name()
                )
                .build();
    }


    // ==========================================
    // REGISTER
    // ==========================================

    @Override
    public UserResponse register(RegisterRequest request) {

        logger.info(
                "Registration request received for username: {}",
                request.getUsername()
        );


        // ================================
        // Username validation
        // ================================

        if (userRepository.existsByUsername(
                request.getUsername()
        )) {

            logger.warn(
                    "Registration failed. Username already exists: {}",
                    request.getUsername()
            );

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
                request.getEmail()
        )) {

            logger.warn(
                    "Registration failed. Email already exists: {}",
                    request.getEmail()
            );

            throw new DuplicateResourceException(
                    "Email already exists"
            );
        }


        // ================================
        // Get VIEWER role
        // ================================

        Role viewerRole = roleRepository
                .findByRoleName(RoleName.VIEWER)
                .orElseThrow(() -> {

                    logger.error(
                            "Registration failed. VIEWER role not configured"
                    );

                    return new ResourceNotFoundException(
                            "VIEWER role not configured"
                    );
                });


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


        User savedUser =
                userRepository.save(user);


        logger.info(
                "User registered successfully. Username: {}, ID: {}",
                savedUser.getUsername(),
                savedUser.getId()
        );


        // ================================
        // Response
        // ================================

        return UserResponse.builder()
                .id(savedUser.getId())
                .username(savedUser.getUsername())
                .fullName(savedUser.getFullName())
                .email(savedUser.getEmail())
                .mobileNo(savedUser.getMobileNo())
                .roleName(
                        savedUser.getRole()
                                .getRoleName()
                                .name()
                )
                .active(savedUser.getActive())
                .build();
    }


    // ==========================================
    // FORGOT PASSWORD
    // ==========================================

    @Override
    public void forgotPassword(
            ForgotPasswordRequest request
    ) {

        logger.info(
                "Forgot password request received for email: {}",
                request.getEmail()
        );


        User user = userRepository
                .findByEmail(request.getEmail())
                .orElseThrow(() -> {

                    logger.warn(
                            "Forgot password failed. No user found for email: {}",
                            request.getEmail()
                    );

                    return new ResourceNotFoundException(
                            "No user found with this email"
                    );
                });


        // Generate secure reset token

        String token =
                UUID.randomUUID().toString();


        // Token expires after 15 minutes

        user.setResetPasswordToken(token);

        user.setResetPasswordTokenExpiry(
                LocalDateTime.now()
                        .plusMinutes(15)
        );


        userRepository.save(user);


        // Temporary reset link for testing.
        // Later this will be sent using email.

        String resetLink =
                "http://localhost:5173/reset-password?token="
                        + token;


        logger.info(
                "Password reset token generated for email: {}",
                request.getEmail()
        );


        logger.debug(
                "Password reset link: {}",
                resetLink
        );
    }


    // ==========================================
    // RESET PASSWORD
    // ==========================================

    @Override
    public void resetPassword(
            ResetPasswordRequest request
    ) {

        logger.info(
                "Password reset request received"
        );


        User user = userRepository
                .findByResetPasswordToken(
                        request.getToken()
                )
                .orElseThrow(() -> {

                    logger.warn(
                            "Password reset failed. Invalid token"
                    );

                    return new ResourceNotFoundException(
                            "Invalid password reset token"
                    );
                });


        // ================================
        // Validate token expiry
        // ================================

        if (user.getResetPasswordTokenExpiry() == null
                || user.getResetPasswordTokenExpiry()
                .isBefore(LocalDateTime.now())) {


            logger.warn(
                    "Password reset failed. Token expired for username: {}",
                    user.getUsername()
            );


            // Remove expired token

            user.setResetPasswordToken(null);

            user.setResetPasswordTokenExpiry(null);

            userRepository.save(user);


            throw new ResourceNotFoundException(
                    "Password reset token has expired"
            );
        }


        // ================================
        // Update password
        // ================================

        user.setPassword(
                passwordEncoder.encode(
                        request.getNewPassword()
                )
        );


        // ================================
        // Remove token after successful reset
        // ================================

        user.setResetPasswordToken(null);

        user.setResetPasswordTokenExpiry(null);


        userRepository.save(user);


        logger.info(
                "Password reset successful for username: {}",
                user.getUsername()
        );
    }

}
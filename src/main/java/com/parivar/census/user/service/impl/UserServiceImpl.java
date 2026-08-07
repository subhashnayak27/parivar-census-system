package com.parivar.census.user.service.impl;

import com.parivar.census.common.dto.PageResponse;
import com.parivar.census.common.dto.PaginationRequest;
import com.parivar.census.common.util.PageResponseUtil;
import com.parivar.census.common.util.PaginationUtil;
import com.parivar.census.exception.DuplicateResourceException;
import com.parivar.census.exception.ResourceNotFoundException;
import com.parivar.census.role.entity.Role;
import com.parivar.census.role.repository.RoleRepository;
import com.parivar.census.user.dto.request.UserRequest;
import com.parivar.census.user.dto.response.UserResponse;
import com.parivar.census.user.entity.User;
import com.parivar.census.user.repository.UserRepository;
import com.parivar.census.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserResponse createUser(UserRequest request) {

        log.info("Creating user {}", request.getUsername());

        if (userRepository.existsByUsername(request.getUsername())) {
            throw new DuplicateResourceException(
                    "Username already exists : " + request.getUsername());
        }

        if (request.getEmail() != null
                && !request.getEmail().isBlank()
                && userRepository.existsByEmail(request.getEmail())) {

            throw new DuplicateResourceException(
                    "Email already exists : " + request.getEmail());
        }

        Role role = roleRepository.findById(request.getRoleId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Role not found : " + request.getRoleId()));

        User user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .fullName(request.getFullName())
                .email(request.getEmail())
                .mobileNo(request.getMobileNo())
                .role(role)
                .active(true)
                .build();

        User saved = userRepository.save(user);

        log.info("User created successfully {}", saved.getId());

        return mapToResponse(saved);
    }

    @Override
    public UserResponse updateUser(Long id, UserRequest request) {

        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found : " + id));

        if (request.getEmail() != null
                && !request.getEmail().isBlank()
                && !request.getEmail().equals(user.getEmail())
                && userRepository.existsByEmail(request.getEmail())) {

            throw new DuplicateResourceException(
                    "Email already exists : " + request.getEmail());
        }

        Role role = roleRepository.findById(request.getRoleId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Role not found"));

        user.setUsername(request.getUsername());

        if (request.getPassword() != null &&
                !request.getPassword().isBlank()) {

            user.setPassword(
                    passwordEncoder.encode(request.getPassword()));
        }

        user.setFullName(request.getFullName());
        user.setEmail(request.getEmail());
        user.setMobileNo(request.getMobileNo());
        user.setRole(role);

        return mapToResponse(userRepository.save(user));
    }

    @Override
    public UserResponse getUserById(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found : " + id));

        return mapToResponse(user);
    }

    @Override
    public PageResponse<UserResponse> getAllUsers(
            PaginationRequest request) {

        log.info("Fetching users. Page: {}, Size: {}",
                request.getPage(),
                request.getSize());

        Pageable pageable = PaginationUtil.getPageable(request);

        Page<User> userPage = userRepository.findByActiveTrue(pageable);

        List<UserResponse> response = userPage.getContent()
                .stream()
                .map(this::mapToResponse)
                .toList();

        return PageResponseUtil.of(userPage, response);
    }

    @Override
    public void deleteUser(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found : " + id));

        user.setActive(false);

        userRepository.save(user);
    }

    private UserResponse mapToResponse(User user) {

        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .mobileNo(user.getMobileNo())
                .roleName(user.getRole().getRoleName().name())
                .active(user.getActive())
                .build();
    }
}
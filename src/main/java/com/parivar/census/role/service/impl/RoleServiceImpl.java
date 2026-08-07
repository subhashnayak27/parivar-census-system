package com.parivar.census.role.service.impl;

import com.parivar.census.exception.DuplicateResourceException;
import com.parivar.census.exception.ResourceNotFoundException;
import com.parivar.census.role.dto.request.RoleRequest;
import com.parivar.census.role.dto.response.RoleResponse;
import com.parivar.census.role.entity.Role;
import com.parivar.census.role.repository.RoleRepository;
import com.parivar.census.role.service.RoleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    @Override
    public RoleResponse createRole(RoleRequest request) {

        log.info("Creating role {}", request.getRoleName());

        if (roleRepository.existsByRoleName(request.getRoleName())) {

            throw new DuplicateResourceException(
                    "Role already exists : " + request.getRoleName());
        }

        Role role = Role.builder()
                .roleName(request.getRoleName())
                .description(request.getDescription())
                .build();

        Role saved = roleRepository.save(role);

        log.info("Role created successfully {}", saved.getId());

        return mapToResponse(saved);
    }

    @Override
    public RoleResponse updateRole(Long id, RoleRequest request) {

        log.info("Updating role {}", id);

        Role role = roleRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Role not found with id : " + id));

        if (!role.getRoleName().equals(request.getRoleName())
                && roleRepository.existsByRoleName(request.getRoleName())) {

            throw new DuplicateResourceException(
                    "Role already exists : " + request.getRoleName());
        }

        role.setRoleName(request.getRoleName());
        role.setDescription(request.getDescription());

        Role updated = roleRepository.save(role);

        log.info("Role updated successfully {}", updated.getId());

        return mapToResponse(updated);
    }

    @Override
    public RoleResponse getRoleById(Long id) {

        log.info("Fetching role {}", id);

        Role role = roleRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Role not found with id : " + id));

        if (!role.getActive()) {

            throw new ResourceNotFoundException(
                    "Role not found with id : " + id);
        }

        return mapToResponse(role);
    }

    @Override
    public List<RoleResponse> getAllRoles() {

        log.info("Fetching all active roles");

        return roleRepository.findByActiveTrue()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public void deleteRole(Long id) {

        log.info("Deleting role {}", id);

        Role role = roleRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Role not found with id : " + id));

        role.setActive(false);

        roleRepository.save(role);

        log.info("Role deleted successfully {}", id);
    }

    private RoleResponse mapToResponse(Role role) {

        return RoleResponse.builder()
                .id(role.getId())
                .roleName(role.getRoleName())
                .description(role.getDescription())
                .active(role.getActive())
                .build();
    }
}
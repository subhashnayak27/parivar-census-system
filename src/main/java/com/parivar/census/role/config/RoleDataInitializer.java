package com.parivar.census.role.config;

import com.parivar.census.role.entity.Role;
import com.parivar.census.role.enums.RoleName;
import com.parivar.census.role.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RoleDataInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;

    @Override
    public void run(String... args) {

        createRole(RoleName.SUPER_ADMIN, "Full System Access");
        createRole(RoleName.ADMIN, "Limited Administration");
        createRole(RoleName.DATA_ENTRY, "Family & Member Entry");
        createRole(RoleName.VIEWER, "Read Only Access");

    }

    private void createRole(RoleName roleName, String description) {

        if (!roleRepository.existsByRoleName(roleName)) {

            roleRepository.save(
                    Role.builder()
                            .roleName(roleName)
                            .description(description)
                            .active(true)
                            .build());

        }
    }
}
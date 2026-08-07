package com.parivar.census.config;

import com.parivar.census.role.entity.Role;
import com.parivar.census.role.enums.RoleName;
import com.parivar.census.role.repository.RoleRepository;
import com.parivar.census.user.entity.User;
import com.parivar.census.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {

        createRole(RoleName.SUPER_ADMIN, "System Owner");
        createRole(RoleName.ADMIN, "System Administrator");
        createRole(RoleName.DATA_ENTRY, "Data Entry Operator");
        createRole(RoleName.VIEWER, "Read Only User");

        createDefaultSuperAdmin();
    }

    private void createRole(RoleName roleName, String description) {

        if (!roleRepository.existsByRoleName(roleName)) {

            Role role = Role.builder()
                    .roleName(roleName)
                    .description(description)
                    .active(true)
                    .build();

            roleRepository.save(role);

            System.out.println("Created Role : " + roleName);
        }
    }

    private void createDefaultSuperAdmin() {

        if (userRepository.findByUsername("superadmin").isPresent()) {
            return;
        }

        Role superAdminRole = roleRepository
                .findByRoleName(RoleName.SUPER_ADMIN)
                .orElseThrow(() ->
                        new RuntimeException("SUPER_ADMIN role not found"));

        User user = User.builder()
                .username("superadmin")
                .password(passwordEncoder.encode("super123"))
                .fullName("System Owner")
                .email("superadmin@parivar.com")
                .mobileNo("9999999999")
                .role(superAdminRole)
                .active(true)
                .build();

        userRepository.save(user);

        System.out.println("Default SUPER_ADMIN created.");
    }
}
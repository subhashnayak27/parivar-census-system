package com.parivar.census.security.service;

import com.parivar.census.user.entity.User;
import com.parivar.census.user.repository.UserRepository;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() ->
                        new UsernameNotFoundException(
                                "User not found : " + username));

        if (!Boolean.TRUE.equals(user.getActive())) {
            throw new UsernameNotFoundException(
                    "User is inactive");
        }

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                List.of(
                        new SimpleGrantedAuthority(
                                "ROLE_" + user.getRole().getRoleName().name()
                        )
                )
        );
    }
}
package com.example.demo.service.impl;

import com.example.demo.entity.auth.AuthUser;
import com.example.demo.sercurity.UserPrincipal;
import com.example.demo.repository.AuthUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UserDetailsServiceImpl implements UserDetailsService {
    private final AuthUserRepository authUserRepository;

    @Override
    public UserDetails loadUserByUsername(String emailOrUsername) throws UsernameNotFoundException {
        AuthUser authUser = authUserRepository.findByEmailOrUsername(emailOrUsername, emailOrUsername).orElseThrow(() -> new UsernameNotFoundException("Username or Email not found"));
        return new UserPrincipal(
                authUser.getId(),
                authUser.getUsername(),
                authUser.getEmail(),
                authUser.getPassword(),
                authUser.getAuthorities(),
                authUser.isStatus(),
                authUser.getExpireDate(),
                authUser.getDevices()
        );
    }
}

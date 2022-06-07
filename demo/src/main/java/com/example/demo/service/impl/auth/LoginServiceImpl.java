package com.example.demo.service.impl.auth;

import com.example.demo.constants.HttpStatusConstants;
import com.example.demo.dto.CustomUserDetails;
import com.example.demo.entity.auth.AuthUser;
import com.example.demo.exceptions.BusinessException;
import com.example.demo.repository.AuthUserRepository;
import com.example.demo.service.auth.LoginService;
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
public class LoginServiceImpl implements UserDetailsService {

    private final AuthUserRepository authUserRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AuthUser authUser = authUserRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("Username not found"));
        return new CustomUserDetails(
                authUser.getUsername(),
                authUser.getEmail(),
                authUser.getPassword(),
                authUser.getAuthorities(),
                authUser.isStatus(),
                authUser.getExpireDate(),
                authUser.getDevices()
        );
    }


//    @Override
//    public void loginFail(String username) {
//        AuthUser authUser = authUserRepository.findByUsername(username).orElseThrow(
//                () -> new BusinessException(HttpStatusConstants.USERNAME_EXIST_CODE, HttpStatusConstants.USERNAME_EXIST_MESSAGE));
//        int count = authUser.getLoginFailCount();
//        if (++count == 5) {
//            authUser.setStatus(false);
//        }
//        authUser.setLoginFailCount(count);
//        authUserRepository.save(authUser);
//    }
}

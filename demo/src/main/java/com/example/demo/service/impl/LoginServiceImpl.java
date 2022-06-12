package com.example.demo.service.impl;

import com.example.demo.constants.HttpStatusConstants;
import com.example.demo.entity.auth.AuthUser;
import com.example.demo.exceptions.BusinessException;
import com.example.demo.repository.AuthUserRepository;
import com.example.demo.service.LoginService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class LoginServiceImpl implements LoginService {

    private final AuthUserRepository authUserRepository;

    @Override
    public void loginFail(String emailOrUsername) {
        AuthUser authUser = authUserRepository.findByEmailOrUsername(emailOrUsername, emailOrUsername).orElseThrow(
                () -> new BusinessException(HttpStatusConstants.USERNAME_NOT_EXISTED_CODE, HttpStatusConstants.USERNAME_NOT_EXISTED_MESSAGE));
        int count = authUser.getLoginFailCount();
        if (++count == 5) {
            authUser.setStatus(false);
        }
        authUser.setLoginFailCount(count);
        authUserRepository.save(authUser);
    }
}

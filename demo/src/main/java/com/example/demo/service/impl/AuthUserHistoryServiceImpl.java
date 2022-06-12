package com.example.demo.service.impl;

import com.example.demo.constants.HttpStatusConstants;
import com.example.demo.entity.auth.AuthUser;
import com.example.demo.entity.auth.AuthUserHistory;
import com.example.demo.enums.EActionName;
import com.example.demo.exceptions.BusinessException;
import com.example.demo.repository.AuthUserHistoryRepository;
import com.example.demo.repository.AuthUserRepository;
import com.example.demo.service.AuthUserHistoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class AuthUserHistoryServiceImpl implements AuthUserHistoryService {

    private final AuthUserHistoryRepository userHistoryRepository;

    private final AuthUserRepository authUserRepository;


    @Override
    public void log(String emailOrUsername, EActionName actionName, boolean actionStatus) {
        AuthUser authUser = authUserRepository.findByEmailOrUsername(emailOrUsername, emailOrUsername).orElseThrow(
                () -> new BusinessException(HttpStatusConstants.USERNAME_NOT_EXISTED_CODE, HttpStatusConstants.USERNAME_NOT_EXISTED_MESSAGE)
        );
        AuthUserHistory authUserHistory = new AuthUserHistory(
                authUser.getUsername(),
                authUser.getEmail(),
                authUser.getDisplayName(),
                authUser.getPassword(),
                actionName,
                actionStatus,
                authUser
        );
        userHistoryRepository.save(authUserHistory);
    }


}

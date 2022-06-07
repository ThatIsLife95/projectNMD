package com.example.demo.service.impl.auth;

import com.example.demo.constants.HttpStatusConstants;
import com.example.demo.entity.auth.AuthDevice;
import com.example.demo.entity.auth.AuthUser;
import com.example.demo.exceptions.BusinessException;
import com.example.demo.repository.AuthUserRepository;
import com.example.demo.service.auth.AuthDeviceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class AuthDeviceServiceImpl implements AuthDeviceService {

    private final AuthUserRepository userRepository;


    @Override
    public void addNewDevice(String username, String deviceLocation, String deviceDetails) {
        AuthDevice authDevice = new AuthDevice(
                deviceLocation,
                deviceDetails,
                false
        );
        AuthUser authUser = userRepository.findByUsername(username).orElseThrow(
                () -> new BusinessException(HttpStatusConstants.USERNAME_EXIST_CODE, HttpStatusConstants.USERNAME_EXIST_MESSAGE));
        authUser.addDevice(authDevice);
        userRepository.save(authUser);
    }
}

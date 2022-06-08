package com.example.demo.service.impl;

import com.example.demo.constants.HttpStatusConstants;
import com.example.demo.entity.auth.AuthDevice;
import com.example.demo.entity.auth.AuthUser;
import com.example.demo.exceptions.BusinessException;
import com.example.demo.repository.AuthDeviceRepository;
import com.example.demo.repository.AuthUserRepository;
import com.example.demo.service.AuthDeviceService;
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

    private final AuthDeviceRepository deviceRepository;


    @Override
    public void addNewDevice(String username, String deviceLocation, String deviceDetails) {
        AuthDevice device = deviceRepository.findByUser_UsernameAndDeviceLocationAndDeviceDetails(username, deviceLocation, deviceDetails).orElse(
                new AuthDevice(deviceLocation, deviceDetails, false)
        );
        AuthUser authUser = userRepository.findByUsername(username).orElseThrow(
                () -> new BusinessException(HttpStatusConstants.USERNAME_NOT_EXISTED_CODE, HttpStatusConstants.USERNAME_NOT_EXISTED_MESSAGE));
        authUser.addDevice(device);
        deviceRepository.save(device);
    }

    @Override
    public void activateNewDevice(String deviceLocation, String deviceDetails, String email) {
        AuthDevice device = deviceRepository.findByDeviceLocationAndDeviceDetailsAndUser_Email(deviceLocation, deviceDetails, email).orElseThrow(
                () -> new BusinessException(HttpStatusConstants.DEVICE_NOT_EXISTED_CODE, HttpStatusConstants.DEVICE_NOT_EXISTED_MESSAGE)
        );
        device.setStatus(true);
        deviceRepository.save(device);
    }
}

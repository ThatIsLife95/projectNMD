package com.example.demo.service.impl;

import com.example.demo.constants.DefaultConstants;
import com.example.demo.constants.HttpStatusConstants;
import com.example.demo.dto.RegistrationDto;
import com.example.demo.entity.auth.AuthDevice;
import com.example.demo.entity.auth.AuthRole;
import com.example.demo.entity.auth.AuthUser;
import com.example.demo.exceptions.BusinessException;
import com.example.demo.repository.AuthRoleRepository;
import com.example.demo.repository.AuthUserRepository;
import com.example.demo.service.RegistrationService;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class RegistrationServiceImpl implements RegistrationService {

    private final AuthUserRepository userRepository;

    private final AuthRoleRepository roleRepository;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public boolean isExistedEmail(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    @Override
    public boolean isExistedUsername(String username) {
        return userRepository.findByUsername(username).isPresent();
    }

    @Override
    public void registerUser(RegistrationDto registrationDto, String deviceLocation, String deviceDetails) {
        String email = registrationDto.getEmail();
        String username = registrationDto.getUsername();
        if (isExistedEmail(email)) {
            throw new BusinessException(HttpStatusConstants.EMAIL_EXIST_CODE, HttpStatusConstants.EMAIL_EXIST_MESSAGE);
        } else if (isExistedUsername(username)) {
            throw new BusinessException(HttpStatusConstants.USERNAME_EXIST_CODE, HttpStatusConstants.USERNAME_EXIST_MESSAGE);
        } else {
            AuthUser user = new AuthUser(
                    registrationDto.getUsername(),
                    registrationDto.getDisplayName(),
                    registrationDto.getEmail(),
                    bCryptPasswordEncoder.encode(registrationDto.getPassword()),
                    true,
                    LocalDateTime.now().plusMonths(DefaultConstants.EXPIRE_MONTH_PASSWORD)
            );
            AuthDevice device = new AuthDevice(
                    deviceLocation,
                    deviceDetails,
                    true
            );
            user.addDevice(device);
            AuthRole authRole = roleRepository.findByName("USER").orElseThrow(() ->
                    new BusinessException(HttpStatusConstants.ROLE_NOT_EXISTED_CODE, HttpStatusConstants.ROLE_NOT_EXISTED_MESSAGE)
            );
            user.addRole(authRole);
            userRepository.save(user);
        }
    }
}

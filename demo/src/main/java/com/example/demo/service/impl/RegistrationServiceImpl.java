package com.example.demo.service.impl;

import com.example.demo.constants.DefaultConstants;
import com.example.demo.constants.HttpStatusConstants;
import com.example.demo.dto.RegistrationDto;
import com.example.demo.entity.UserInfo;
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

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Transactional
@AllArgsConstructor
public class RegistrationServiceImpl implements RegistrationService {

    private final AuthUserRepository userRepository;

    private final AuthRoleRepository roleRepository;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public String isExistedEmail(String email) {
        Optional<AuthUser> user = userRepository.findByEmail(email);
        return user.map(AuthUser::getUsername).orElse(null);
    }

    @Override
    public boolean isExistedUsername(String username) {
        return userRepository.findByUsername(username).isPresent();
    }

    @Override
    public void registerUser(RegistrationDto registrationDto, String deviceLocation, String deviceDetails) {
        String email = registrationDto.getEmail();
        String username = registrationDto.getUsername();
        if (isExistedEmail(email) != null) {
            throw new BusinessException(HttpStatusConstants.EMAIL_EXISTED_CODE, HttpStatusConstants.EMAIL_EXISTED_MESSAGE);
        } else if (isExistedUsername(username)) {
            throw new BusinessException(HttpStatusConstants.USERNAME_EXISTED_CODE, HttpStatusConstants.USERNAME_EXISTED_MESSAGE);
        } else {
            AuthUser user = new AuthUser(
                    registrationDto.getUsername(),
                    registrationDto.getEmail(),
                    bCryptPasswordEncoder.encode(registrationDto.getPassword()),
                    true,
                    LocalDateTime.now().plusMonths(DefaultConstants.EXPIRE_MONTH_PASSWORD)
            );
            UserInfo userInfo = new UserInfo(registrationDto.getDisplayName());
            user.setUserInfo(userInfo);
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

    @Override
    public void resetPassword(String email, String password) {
        AuthUser user = userRepository.findByEmail(email).orElseThrow(
                () -> new BusinessException(HttpStatusConstants.EMAIL_NOT_EXISTED_CODE, HttpStatusConstants.EMAIL_NOT_EXISTED_MESSAGE)
        );
        user.setPassword(bCryptPasswordEncoder.encode(password));
        userRepository.save(user);
    }
}

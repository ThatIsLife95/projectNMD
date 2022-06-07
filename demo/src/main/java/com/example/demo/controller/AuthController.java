package com.example.demo.controller;

import com.example.demo.constants.ConstantDefault;
import com.example.demo.constants.HttpStatusConstants;
import com.example.demo.constants.JwtConstants;
import com.example.demo.constants.UriConstants;
import com.example.demo.dto.RegistrationDto;
import com.example.demo.dto.ResponseDto;
import com.example.demo.service.EmailService;
import com.example.demo.service.RegistrationService;
import com.example.demo.service.auth.AuthTokenService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping(path = UriConstants.AUTH_URI)
@AllArgsConstructor
@Slf4j
public class AuthController {

    private final RegistrationService registrationService;

    private final EmailService emailService;

    private final AuthTokenService tokenService;

    @PostMapping(path = "/send-registration-email")
    public ResponseDto<?> sendRegistrationEmail(@Valid @NotNull @Email @RequestBody String email) throws MessagingException, IOException {
        if (registrationService.isExistedEmail(email)) {
            return ResponseDto.error(HttpStatusConstants.EMAIL_EXIST_CODE, HttpStatusConstants.EMAIL_EXIST_MESSAGE);
        }
        String token = UUID.randomUUID().toString();
        Map<String, Object> templateModel = new HashMap<>();
        String link = "http://localhost:8080/tao-tai-khoan?token=" + token;
        templateModel.put("link", link);
        emailService.sendMessage(email, ConstantDefault.REGISTRATION_MAIL_SUBJECT, templateModel);
        tokenService.saveToken(email, token);
        return ResponseDto.ok(null);
    }

    @PostMapping(path = "/register-user")
    public ResponseDto<?> registerUser(@Valid @RequestBody RegistrationDto registrationDto, HttpServletRequest request) {
        String deviceLocation = request.getRemoteAddr();
        String deviceDetails = request.getHeader(JwtConstants.USER_AGENT);
        ResponseDto<?> responseDto = tokenService.verifyToken(registrationDto.getEmail(), registrationDto.getToken());
        if (HttpStatusConstants.SUCCESS_CODE.equals(responseDto.getCode())) {
            registrationService.registerUser(registrationDto, deviceLocation, deviceDetails);
        }
        return responseDto;
    }
}

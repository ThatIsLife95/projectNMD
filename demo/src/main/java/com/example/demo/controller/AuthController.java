package com.example.demo.controller;

import com.example.demo.constants.HttpStatusConstants;
import com.example.demo.constants.JwtConstants;
import com.example.demo.constants.MailTemplateConstants;
import com.example.demo.constants.UriConstants;
import com.example.demo.enums.EActionName;
import com.example.demo.payload.request.EmailRequest;
import com.example.demo.payload.request.LoginRequest;
import com.example.demo.payload.request.RegistrationRequest;
import com.example.demo.payload.request.ResetPasswordRequest;
import com.example.demo.payload.response.ResponseEntity;
import com.example.demo.sercurity.CustomUserDetails;
import com.example.demo.sercurity.JwtTokenProvider;
import com.example.demo.service.*;
import com.example.demo.utils.Common;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(path = UriConstants.AUTH_URI)
@AllArgsConstructor
@Slf4j
public class AuthController {

    private final AuthenticationManager authenticationManager;

    private final RegistrationService registrationService;

    private final EmailService emailService;

    private final AuthTokenService tokenService;

    private final AuthDeviceService deviceService;

    private final AuthUserHistoryService authUserHistoryService;

    private final LoginService loginService;

    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping(path = "/send-registration-email")
    public ResponseEntity<?> sendRegistrationEmail(@Valid @RequestBody EmailRequest emailRequest) throws MessagingException, IOException {
        String email = emailRequest.getEmail();
        if (registrationService.isExistedEmail(email) != null) {
            return ResponseEntity.error(HttpStatusConstants.EMAIL_EXISTED_CODE, HttpStatusConstants.EMAIL_EXISTED_MESSAGE);
        }
        String token = Common.getToken();
        String link = MailTemplateConstants.REGISTRATION_MAIL_LINK + "?email=" + email + "&token=" + token;
        Map<String, Object> templateModel = Common.getTemplateModel(
                link,
                MailTemplateConstants.REGISTRATION_MAIL_GREETING,
                MailTemplateConstants.REGISTRATION_MAIL_TEXT,
                MailTemplateConstants.REGISTRATION_MAIL_BUTTON
        );
        emailService.sendMessage(email, MailTemplateConstants.REGISTRATION_MAIL_SUBJECT, templateModel);
        tokenService.saveToken(email, token);
        return ResponseEntity.ok(null);
    }

    @PostMapping(path = "/register-user")
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegistrationRequest registrationRequest, HttpServletRequest request) {
        String deviceLocation = request.getRemoteAddr();
        String deviceDetails = request.getHeader(JwtConstants.USER_AGENT);
        ResponseEntity<?> responseEntity = tokenService.verifyToken(registrationRequest.getEmail(), registrationRequest.getToken());
        if (HttpStatusConstants.SUCCESS_CODE.equals(responseEntity.getCode())) {
            registrationService.registerUser(registrationRequest, deviceLocation, deviceDetails);
        }
        return responseEntity;
    }

    @PostMapping(path = "/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest, HttpServletRequest request) throws MessagingException, IOException {
        String deviceLocation = request.getRemoteAddr();
        String deviceDetails = request.getHeader(JwtConstants.USER_AGENT);
        boolean actionStatus = false;
        String emailOrUsername = loginRequest.getEmailOrUsername();
        try {
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(emailOrUsername, loginRequest.getPassword());
            Authentication authentication = authenticationManager.authenticate(authenticationToken);
            CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();
            String username = user.getUsername();
//            deviceLocation = "192.168.1.1";
//            deviceDetails = "IP 7";
            if (user.isNewDevice(deviceLocation, deviceDetails)) {
                // Thêm thiết bị mới
                deviceService.addNewDevice(username, deviceLocation, deviceDetails);
                String token = Common.getToken();
                String email = user.getEmail();
                String link = MailTemplateConstants.VERIFY_NEW_DEVICE_MAIL_LINK + "?email=" + email + "&deviceLocation=" + deviceLocation + "&deviceDetails=" + deviceDetails + "&token=" + token;
                Map<String, Object> templateModel = Common.getTemplateModel(
                        link,
                        MailTemplateConstants.VERIFY_NEW_DEVICE_MAIL_GREETING + username,
                        MailTemplateConstants.VERIFY_NEW_DEVICE_MAIL_TEXT,
                        MailTemplateConstants.VERIFY_NEW_DEVICE_MAIL_BUTTON
                );
                emailService.sendMessage(email, MailTemplateConstants.VERIFY_NEW_DEVICE_MAIL_SUBJECT, templateModel);
                tokenService.saveToken(email, token);
                return ResponseEntity.buildAll(HttpStatusConstants.VERIFY_NEW_DEVICE_CODE,
                        HttpStatusConstants.VERIFY_NEW_DEVICE_MESSAGE,
                        null);
            }
            String accessToken = jwtTokenProvider.generateAccessToken(user, request);
            String refreshToken = jwtTokenProvider.generateRefreshToken(user, request);
            Map<String, String> tokens = new HashMap<>();
            tokens.put("access_token", accessToken);
            tokens.put("refresh_token", refreshToken);
            // đăng nhập thành công đổi trạng thái action
            actionStatus = true;
            return ResponseEntity.ok(tokens);
        } catch (LockedException e) {
            log.error("LockedException => rootCause: {}", Arrays.stream(e.getStackTrace()).findFirst());
            log.error("LockedException => localizedMessage: {}", e.getMessage());
            return ResponseEntity.error(HttpStatusConstants.LOCKED_ACCOUNT_CODE, HttpStatusConstants.LOCKED_ACCOUNT_MESSAGE);
        } catch (UsernameNotFoundException | AuthenticationCredentialsNotFoundException e) {
            log.error("Exception => rootCause: {}", Arrays.stream(e.getStackTrace()).findFirst());
            log.error("Exception => localizedMessage: {}", e.getMessage());
            loginService.loginFail(emailOrUsername);
            return ResponseEntity.error(HttpStatusConstants.INVALID_EMAIL_OR_PASSWORD_CODE, HttpStatusConstants.INVALID_EMAIL_OR_PASSWORD_MESSAGE);
        } catch (AccountExpiredException e) {
            log.error("AccountExpiredException => rootCause: {}", Arrays.stream(e.getStackTrace()).findFirst());
            log.error("AccountExpiredException => localizedMessage: {}", e.getMessage());
            return ResponseEntity.error(HttpStatusConstants.EXPIRED_PASSWORD_CODE, HttpStatusConstants.EXPIRED_PASSWORD_MESSAGE);
        } catch (AuthenticationException e) {
            log.error("AuthenticationException => rootCause: {}", Arrays.stream(e.getStackTrace()).findFirst());
            log.error("AuthenticationException => localizedMessage: {}", e.getMessage());
            return ResponseEntity.error(HttpStatusConstants.UNAVAILABLE_CODE, HttpStatusConstants.UNAVAILABLE_MESSAGE);
        } finally {
            // ghi log ra database
            authUserHistoryService.log(emailOrUsername, EActionName.LOGIN, actionStatus);
        }
    }

    @GetMapping(path = "/activate-new-device")
    public ResponseEntity<?> activateNewDevice(@RequestParam String email,
                                               @RequestParam String deviceLocation,
                                               @RequestParam String deviceDetails,
                                               @RequestParam String token) {
        ResponseEntity<?> responseEntity = tokenService.verifyToken(email, token);
        if (HttpStatusConstants.SUCCESS_CODE.equals(responseEntity.getCode())) {
            deviceService.activateNewDevice(deviceLocation, deviceDetails, email);
        }
        return responseEntity;
    }

    @PostMapping(path = "/forgot-password")
    public ResponseEntity<?> forgotPassword(@Valid @RequestBody EmailRequest emailRequest) throws MessagingException, IOException {
        String email = emailRequest.getEmail();
        String username = registrationService.isExistedEmail(email);
        if (username == null) {
            return ResponseEntity.error(HttpStatusConstants.EMAIL_NOT_EXISTED_CODE, HttpStatusConstants.EMAIL_NOT_EXISTED_MESSAGE);
        }
        String token = Common.getToken();
        String link = MailTemplateConstants.FORGOT_PASSWORD_MAIL_LINK + "?email=" + email + "&token=" + token;
        Map<String, Object> templateModel = Common.getTemplateModel(
                link,
                MailTemplateConstants.FORGOT_PASSWORD_MAIL_GREETING + username,
                MailTemplateConstants.FORGOT_PASSWORD_MAIL_TEXT,
                MailTemplateConstants.FORGOT_PASSWORD_MAIL_BUTTON
        );
        emailService.sendMessage(email, MailTemplateConstants.FORGOT_PASSWORD_MAIL_SUBJECT, templateModel);
        tokenService.saveToken(email, token);
        return ResponseEntity.ok(null);
    }

    @PostMapping(path = "/reset-password")
    public ResponseEntity<?> resetPassword(@Valid @RequestBody ResetPasswordRequest resetPasswordRequest) {
        ResponseEntity<?> responseEntity = tokenService.verifyToken(resetPasswordRequest.getEmail(), resetPasswordRequest.getToken());
        if (HttpStatusConstants.SUCCESS_CODE.equals(responseEntity.getCode())) {
            registrationService.resetPassword(resetPasswordRequest.getEmail(), resetPasswordRequest.getPassword());
            responseEntity = ResponseEntity.ok(null);
        }
        return responseEntity;
    }


}

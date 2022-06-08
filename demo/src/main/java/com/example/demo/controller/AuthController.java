package com.example.demo.controller;

import com.example.demo.constants.HttpStatusConstants;
import com.example.demo.constants.JwtConstants;
import com.example.demo.constants.MailTemplateConstants;
import com.example.demo.constants.UriConstants;
import com.example.demo.dto.*;
import com.example.demo.enums.EActionName;
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
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
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

    @PostMapping(path = "/send-registration-email")
    public ResponseDto<?> sendRegistrationEmail(@Valid @NotNull @Email @RequestBody String email) throws MessagingException, IOException {
        if (registrationService.isExistedEmail(email) != null) {
            return ResponseDto.error(HttpStatusConstants.EMAIL_EXISTED_CODE, HttpStatusConstants.EMAIL_EXISTED_MESSAGE);
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

    @PostMapping(path = "/login")
    public ResponseDto<?> login(@RequestBody LoginDto loginDto, HttpServletRequest request) throws MessagingException, IOException {
        String deviceLocation = request.getRemoteAddr();
        String deviceDetails = request.getHeader(JwtConstants.USER_AGENT);
        boolean actionStatus = false;
        String username = loginDto.getUsername();
        try {
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, loginDto.getPassword());
            Authentication authentication = authenticationManager.authenticate(authenticationToken);
            CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();
            deviceLocation = "192.168.1.1";
            deviceDetails = "IP 7";
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
                return ResponseDto.buildAll(HttpStatusConstants.VERIFY_NEW_DEVICE_CODE,
                        HttpStatusConstants.VERIFY_NEW_DEVICE_MESSAGE,
                        null);
            }
            String accessToken = Common.getJWT(user, request, JwtConstants.ACCESS_TOKEN_EXPIRATION_TIME);
            String refreshToken = Common.getJWT(user, request, JwtConstants.REFRESH_TOKEN_EXPIRATION_TIME);
            Map<String, String> tokens = new HashMap<>();
            tokens.put("access_token", accessToken);
            tokens.put("refresh_token", refreshToken);
            // đăng nhập thành công đổi trạng thái action
            actionStatus = true;
            return ResponseDto.ok(tokens);
        } catch (LockedException e) {
            log.error("LockedException => rootCause: {}", Arrays.stream(e.getStackTrace()).findFirst());
            log.error("LockedException => localizedMessage: {}", e.getMessage());
            return ResponseDto.error(HttpStatusConstants.LOCKED_ACCOUNT_CODE, HttpStatusConstants.LOCKED_ACCOUNT_MESSAGE);
        } catch (UsernameNotFoundException | AuthenticationCredentialsNotFoundException e) {
            log.error("Exception => rootCause: {}", Arrays.stream(e.getStackTrace()).findFirst());
            log.error("Exception => localizedMessage: {}", e.getMessage());
            loginService.loginFail(username);
            return ResponseDto.error(HttpStatusConstants.INVALID_EMAIL_OR_PASSWORD_CODE, HttpStatusConstants.INVALID_EMAIL_OR_PASSWORD_MESSAGE);
        } catch (AccountExpiredException e) {
            log.error("AccountExpiredException => rootCause: {}", Arrays.stream(e.getStackTrace()).findFirst());
            log.error("AccountExpiredException => localizedMessage: {}", e.getMessage());
            return ResponseDto.error(HttpStatusConstants.EXPIRED_PASSWORD_CODE, HttpStatusConstants.EXPIRED_PASSWORD_MESSAGE);
        } catch (AuthenticationException e) {
            log.error("AuthenticationException => rootCause: {}", Arrays.stream(e.getStackTrace()).findFirst());
            log.error("AuthenticationException => localizedMessage: {}", e.getMessage());
            return ResponseDto.error(HttpStatusConstants.UNAVAILABLE_CODE, HttpStatusConstants.UNAVAILABLE_MESSAGE);
        } finally {
            UserHistoryDto userHistoryDto = new UserHistoryDto();
            userHistoryDto.setUsername(username);
            userHistoryDto.setActionName(EActionName.LOGIN);
            userHistoryDto.setActionStatus(actionStatus);
            authUserHistoryService.log(userHistoryDto);
        }
    }

    @GetMapping(path = "/activate-new-device")
    public ResponseDto<?> activateNewDevice(@RequestParam String email,
                                            @RequestParam String deviceLocation,
                                            @RequestParam String deviceDetails,
                                            @RequestParam String token) {
        ResponseDto<?> responseDto = tokenService.verifyToken(email, token);
        if (HttpStatusConstants.SUCCESS_CODE.equals(responseDto.getCode())) {
            deviceService.activateNewDevice(deviceLocation, deviceDetails, email);
        }
        return responseDto;
    }

    @PostMapping(path = "/forgot-password")
    public ResponseDto<?> forgotPassword(@Valid @NotNull @Email @RequestBody String email) throws MessagingException, IOException {
        String username = registrationService.isExistedEmail(email);
        if (username == null) {
            return ResponseDto.error(HttpStatusConstants.EMAIL_NOT_EXISTED_CODE, HttpStatusConstants.EMAIL_NOT_EXISTED_MESSAGE);
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
        return ResponseDto.ok(null);
    }

    @PostMapping(path = "/reset-password")
    public ResponseDto<?> resetPassword(@Valid @RequestBody ResetPasswordDto resetPasswordDto) {
        ResponseDto<?> responseDto = tokenService.verifyToken(resetPasswordDto.getEmail(), resetPasswordDto.getToken());
        if (HttpStatusConstants.SUCCESS_CODE.equals(responseDto.getCode())) {
            registrationService.resetPassword(resetPasswordDto.getEmail(), resetPasswordDto.getPassword());
            responseDto = ResponseDto.ok(null);
        }
        return responseDto;
    }


}

package com.example.demo.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.demo.constants.*;
import com.example.demo.dto.*;
import com.example.demo.entity.auth.EActionName;
import com.example.demo.service.EmailService;
import com.example.demo.service.RegistrationService;
import com.example.demo.service.auth.AuthDeviceService;
import com.example.demo.service.auth.AuthTokenService;
import com.example.demo.service.auth.AuthUserHistoryService;
import com.example.demo.utils.Common;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
import java.util.*;
import java.util.stream.Collectors;

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

//    private final LoginService loginService;



    @PostMapping(path = "/send-registration-email")
    public ResponseDto<?> sendRegistrationEmail(@Valid @NotNull @Email @RequestBody String email) throws MessagingException, IOException {
        if (registrationService.isExistedEmail(email)) {
            return ResponseDto.error(HttpStatusConstants.EMAIL_EXIST_CODE, HttpStatusConstants.EMAIL_EXIST_MESSAGE);
        }
        String token = Common.getToken();
        Map<String, Object> templateModel = Common.getTemplateModel(
                MailTemplateConstants.REGISTRATION_MAIL_LINK,
                token,
                MailTemplateConstants.REGISTRATION_MAIL_GREETING,
                MailTemplateConstants.REGISTRATION_MAIL_TEXT,
                MailTemplateConstants.REGISTRATION_MAIL_BUTTON
                );
        emailService.sendMessage(email, DefaultConstants.REGISTRATION_MAIL_SUBJECT, templateModel);
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
            if (user.isNewDevice(deviceLocation, deviceDetails)) {
                // Thêm thiết bị mới
                deviceService.addNewDevice(username, deviceLocation, deviceDetails);
                String token = Common.getToken();
                Map<String, Object> templateModel = Common.getTemplateModel(
                        MailTemplateConstants.VERIFY_NEW_DEVICE_MAIL_LINK,
                        token,
                        MailTemplateConstants.VERIFY_NEW_DEVICE_MAIL_GREETING,
                        MailTemplateConstants.VERIFY_NEW_DEVICE_MAIL_TEXT,
                        MailTemplateConstants.VERIFY_NEW_DEVICE_MAIL_BUTTON
                );
                String email = user.getEmail();
                emailService.sendMessage(email, DefaultConstants.VERIFY_NEW_DEVICE_MAIL_SUBJECT, templateModel);
                tokenService.saveToken(email, token);
                return ResponseDto.buildAll(HttpStatusConstants.VERIFY_NEW_DEVICE_CODE,
                        HttpStatusConstants.VERIFY_NEW_DEVICE_MESSAGE,
                        null);
            }
            Algorithm algorithm = Algorithm.HMAC256(JwtConstants.SECRET.getBytes());
            String accessToken = JWT.create()
                    .withSubject(user.getUsername())
                    // hạn 12h
                    .withExpiresAt(new Date(System.currentTimeMillis() + JwtConstants.EXPIRATION_TIME))
                    .withIssuer(request.getRequestURL().toString())
                    .withClaim("roles", user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                    .sign(algorithm);
            // đăng nhập thành công đổi trạng thái action
            actionStatus = true;
            return ResponseDto.ok(accessToken);
        } catch (LockedException e) {
            log.error("LockedException => rootCause: {}", Arrays.stream(e.getStackTrace()).findFirst());
            log.error("LockedException => localizedMessage: {}", e.getMessage());
            return ResponseDto.error(HttpStatusConstants.LOCKED_ACCOUNT_CODE, HttpStatusConstants.LOCKED_ACCOUNT_MESSAGE);
        } catch (UsernameNotFoundException | AuthenticationCredentialsNotFoundException e) {
            log.error("Exception => rootCause: {}", Arrays.stream(e.getStackTrace()).findFirst());
            log.error("Exception => localizedMessage: {}", e.getMessage());
//            loginService.loginFail(username);
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
}

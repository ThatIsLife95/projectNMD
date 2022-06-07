package com.example.demo.service.impl.auth;

import com.example.demo.constants.ConstantDefault;
import com.example.demo.constants.HttpStatusConstants;
import com.example.demo.dto.ResponseDto;
import com.example.demo.entity.auth.AuthToken;
import com.example.demo.repository.AuthTokenRepository;
import com.example.demo.service.auth.AuthTokenService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class AuthTokenServiceImpl implements AuthTokenService {

    private final AuthTokenRepository tokenRepository;

    @Override
    public void saveToken(String email, String token) {
        AuthToken authToken = tokenRepository.findByEmail(email).orElse(new AuthToken(email));
        authToken.setToken(token);
        authToken.setConfirmed(false);
        authToken.setExpiredDate(LocalDateTime.now().plusMinutes(ConstantDefault.EXPIRE_MINUTE_LINK));
        tokenRepository.save(authToken);
    }

    @Override
    public ResponseDto<?> verifyToken(String email, String token) {
        ResponseDto<?> responseDto = ResponseDto.ok(null);
        AuthToken authToken = tokenRepository.findByEmail(email).orElseThrow(IllegalStateException::new);
        if (!token.equals(authToken.getToken())) {
            responseDto = ResponseDto.error(HttpStatusConstants.INVALID_LINK_CODE, HttpStatusConstants.INVALID_LINK_CODE_MESSAGE);
        } else if (LocalDateTime.now().isAfter(authToken.getExpiredDate())) {
            responseDto = ResponseDto.error(HttpStatusConstants.EXPIRED_LINK_CODE, HttpStatusConstants.EXPIRED_LINK_CODE_MESSAGE);
        } else {
            authToken.setConfirmed(true);
            tokenRepository.save(authToken);
        }
        return responseDto;
    }
}

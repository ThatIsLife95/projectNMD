package com.example.demo.service.impl;

import com.example.demo.constants.DefaultConstants;
import com.example.demo.constants.HttpStatusConstants;
import com.example.demo.entity.auth.AuthToken;
import com.example.demo.payload.response.ResponseEntity;
import com.example.demo.repository.AuthTokenRepository;
import com.example.demo.service.AuthTokenService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class AuthTokenServiceImpl implements AuthTokenService {

    private final AuthTokenRepository tokenRepository;

    @Override
    public void saveToken(String email, String token) {
        AuthToken authToken = tokenRepository.findByEmail(email).orElse(new AuthToken(email));
        authToken.setToken(token);
        authToken.setStatus(false);
        authToken.setExpiredDate(Instant.now().plusSeconds(DefaultConstants.EXPIRATION_LINK));
        tokenRepository.save(authToken);
    }

    @Override
    public ResponseEntity<?> verifyToken(String email, String token) {
        ResponseEntity<?> responseEntity = ResponseEntity.ok(null);
        AuthToken authToken = tokenRepository.findByEmailAndStatus(email, false).orElseThrow(IllegalStateException::new);
        if (!token.equals(authToken.getToken())) {
            responseEntity = ResponseEntity.error(HttpStatusConstants.INVALID_LINK_CODE, HttpStatusConstants.INVALID_LINK_CODE_MESSAGE);
        } else if (Instant.now().isAfter(authToken.getExpiredDate())) {
            responseEntity = ResponseEntity.error(HttpStatusConstants.EXPIRED_LINK_CODE, HttpStatusConstants.EXPIRED_LINK_CODE_MESSAGE);
        } else {
            authToken.setStatus(true);
            tokenRepository.save(authToken);
        }
        return responseEntity;
    }
}

package com.example.demo.service.auth;

import com.example.demo.dto.ResponseDto;

public interface AuthTokenService {
    public void saveToken(String email, String token);

    public ResponseDto<?> verifyToken(String email, String token);
}

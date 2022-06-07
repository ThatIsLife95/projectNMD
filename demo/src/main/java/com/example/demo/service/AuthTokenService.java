package com.example.demo.service;

import com.example.demo.dto.ResponseDto;

public interface AuthTokenService {
    public void saveToken(String email, String token);

    public ResponseDto<?> verifyToken(String email, String token);
}

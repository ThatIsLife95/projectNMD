package com.example.demo.service;

import com.example.demo.payload.response.ResponseEntity;

public interface AuthTokenService {
    public void saveToken(String email, String token);

    public ResponseEntity<?> verifyToken(String email, String token);
}

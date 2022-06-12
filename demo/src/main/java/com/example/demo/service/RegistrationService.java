package com.example.demo.service;

import com.example.demo.payload.request.RegistrationRequest;

public interface RegistrationService {

    public String isExistedEmail(String email);

    public boolean isExistedUsername(String username);

    public void registerUser(RegistrationRequest registrationRequest, String deviceLocation, String deviceDetails);

    public void resetPassword(String email, String password);
}

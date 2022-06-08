package com.example.demo.service;

import com.example.demo.dto.RegistrationDto;
import com.example.demo.dto.ResponseDto;

public interface RegistrationService {

    public String isExistedEmail(String email);

    public boolean isExistedUsername(String username);

    public void registerUser(RegistrationDto registrationDto, String deviceLocation, String deviceDetails);

    public void resetPassword(String email, String password);
}

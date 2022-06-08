package com.example.demo.service;


public interface AuthDeviceService {
    public void addNewDevice(String username, String deviceLocation, String deviceDetails);

    void activateNewDevice(String deviceLocation, String deviceDetails, String email);
}

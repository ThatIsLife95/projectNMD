package com.example.demo.repository;

import com.example.demo.entity.auth.AuthDevice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AuthDeviceRepository extends JpaRepository<AuthDevice, Integer> {
    Optional<AuthDevice> findByDeviceLocationAndDeviceDetailsAndUser_Email(String deviceLocation, String deviceDetails, String email);

    Optional<AuthDevice> findByUser_UsernameAndDeviceLocationAndDeviceDetails(String username, String deviceLocation, String deviceDetails);

}

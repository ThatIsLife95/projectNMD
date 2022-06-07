package com.example.demo.repository;

import com.example.demo.entity.auth.AuthDevice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthDeviceRepository extends JpaRepository<AuthDevice, Integer> {

}

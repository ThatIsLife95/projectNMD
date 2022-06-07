package com.example.demo.repository;


import com.example.demo.entity.auth.AuthPermission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AuthPermissionRepository extends JpaRepository<AuthPermission, Integer> {
    public Optional<AuthPermission> findByName(String name);
}

package com.example.demo.repository;

import com.example.demo.entity.auth.AuthRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AuthRoleRepository extends JpaRepository<AuthRole, Integer> {
    public Optional<AuthRole> findByName(String name);

}

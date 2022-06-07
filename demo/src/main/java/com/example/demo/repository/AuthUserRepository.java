package com.example.demo.repository;

import com.example.demo.entity.auth.AuthUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthUserRepository extends JpaRepository<AuthUser, Integer> {
    public Optional<AuthUser> findByEmail(String email);

    public Optional<AuthUser> findByUsername(String username);

}

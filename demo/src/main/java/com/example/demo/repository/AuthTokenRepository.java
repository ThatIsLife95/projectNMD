package com.example.demo.repository;

import com.example.demo.entity.auth.AuthToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthTokenRepository extends JpaRepository<AuthToken, Integer> {
    public Optional<AuthToken> findByEmail(String email);
}

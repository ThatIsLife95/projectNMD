package com.example.demo.repository;

import com.example.demo.entity.auth.AuthUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthUserRepository extends JpaRepository<AuthUser, Integer>, JpaSpecificationExecutor<AuthUser> {
    public Optional<AuthUser> findByEmail(String email);

    public Optional<AuthUser> findByEmailOrUsername(String email, String username);

    public Optional<AuthUser> findByUsername(String username);

    Page<AuthUser> findAll(Specification<AuthUser> conditions, Pageable pageable);
}

package com.example.demo.repository;

import com.example.demo.entity.auth.AuthUserHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthUserHistoryRepository extends JpaRepository<AuthUserHistory, Integer> {
}

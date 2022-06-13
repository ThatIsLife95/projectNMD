package com.example.demo.entity.auth;

import com.example.demo.entity.audit.DateAudit;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "auth_token")
public class AuthToken extends DateAudit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private int id;

    @Basic
    @Column(name = "token", nullable = false)
    private String token;

    @Basic
    @Column(name = "email", nullable = false)
    private String email;

    @Basic
    @Column(name = "expired_date")
    private Instant expiredDate;

    @Basic
    @Column(name = "status", nullable = false)
    private boolean status;

    public AuthToken(String email) {
        this.email = email;
    }
}
package com.example.demo.entity.auth;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "auth_token")
public class AuthToken {
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
    private LocalDateTime expiredDate;

    @Basic
    @Column(name = "is_confirmed", nullable = false)
    private boolean isConfirmed;

    public AuthToken(String email) {
        this.email = email;
    }
}
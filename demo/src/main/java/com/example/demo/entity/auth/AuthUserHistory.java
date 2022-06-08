package com.example.demo.entity.auth;

import com.example.demo.enums.EActionName;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "auth_user_history")
public class AuthUserHistory {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private int id;

    @Basic
    @Column(name = "user_name")
    private String username;

    @Basic
    @Column(name = "display_name")
    private String displayName;

    @Basic
    @Column(name = "email")
    private String email;

    @Basic
    @Column(name = "password")
    private String password;

    @Basic
    @Column(name = "action_name")
    private EActionName actionName;

    @Basic
    @Column(name = "action_status")
    private boolean actionStatus;
}

package com.example.demo.entity.auth;

import com.example.demo.entity.audit.DateAudit;
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
public class AuthUserHistory extends DateAudit {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private int id;

    @Basic
    @Column(name = "user_name")
    private String username;

    @Basic
    @Column(name = "email")
    private String email;

    @Basic
    @Column(name = "display_name")
    private String displayName;

    @Basic
    @Column(name = "password")
    private String password;

    @Basic
    @Column(name = "action_name")
    private EActionName actionName;

    @Basic
    @Column(name = "action_status")
    private boolean actionStatus;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private AuthUser user;

    public AuthUserHistory(String username, String email, String displayName, String password, EActionName actionName, boolean actionStatus, AuthUser user) {
        this.username = username;
        this.email = email;
        this.displayName = displayName;
        this.password = password;
        this.actionName = actionName;
        this.actionStatus = actionStatus;
        this.user = user;
    }
}

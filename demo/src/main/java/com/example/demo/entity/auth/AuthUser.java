package com.example.demo.entity.auth;

import com.example.demo.entity.UserInfo;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Getter
@Setter
@Table(name = "auth_user")
@NoArgsConstructor
public class AuthUser {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private int id;

    @Basic
    @Column(name = "username",nullable = false, unique = true)
    private String username;

//    @Basic
//    @Column(name = "display_name")
//    private String displayName;

    @Basic
    @Column(name = "email", unique = true)
    private String email;

    @Basic
    @Column(name = "password")
    private String password;

    @Basic
    @Column(name = "status")
    private boolean status;

    @Basic
    @Column(name = "login_fail_count")
    private int loginFailCount;

    @Basic
    @Column(name = "last_login")
    private LocalDateTime lastLogin;

    @Basic
    @Column(name = "password_expired_date")
    private LocalDateTime expireDate;

    @ManyToMany
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(
                    name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(
                    name = "role_id", referencedColumnName = "id"))
    private Set<AuthRole> roles = new HashSet<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private Set<AuthDevice> devices = new HashSet<>();

    @OneToOne(mappedBy = "authUser", cascade = CascadeType.ALL)
    private UserInfo userInfo;

    public AuthUser(String username, String email, String password, boolean status, LocalDateTime expireDate) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.status = status;
        this.expireDate = expireDate;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
        userInfo.setAuthUser(this);
    }

    public void addDevice(AuthDevice device) {
        devices.add(device);
        device.setUser(this);
    }

    public void addRole(AuthRole role) {
        roles.add(role);
        role.getUsers().add(this);
    }

    public List<SimpleGrantedAuthority> getAuthorities() {
        List<String> authorities = new ArrayList<>();
        Set<AuthPermission> permissions = new HashSet<>();
        for (AuthRole role : roles) {
            authorities.add("ROLE_" + role.getName());
            permissions.addAll(role.getPermissions());
        }
        for (AuthPermission permission : permissions) {
            authorities.add(permission.getName());
        }
        return authorities.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
    }
}

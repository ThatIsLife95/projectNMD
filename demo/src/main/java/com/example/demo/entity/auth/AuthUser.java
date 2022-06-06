package com.example.demo.entity.auth;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Getter
@Setter
@Table(name = "auth_user")
@ToString
@NoArgsConstructor
public class AuthUser {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private int id;

    @Basic
    @Column(name = "user_name",nullable = false, unique = true)
    private String username;

    @Basic
    @Column(name = "display_name")
    private String fullName;

    @Basic
    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Basic
    @Column(name = "password")
    private String password;

    @Basic
    @Column(name = "phone_number")
    private String phoneNumber;

    @Basic
    @Column(name = "identity_card")
    private String identityCard;

    @Basic
    @Column(name = "status")
    private EStatus status;

    @Basic
    @Column(name = "login_fail_count")
    private int loginFailCount;

    @Basic
    @Column(name = "last_login")
    private Timestamp lastLogin;

    @Basic
    @Column(name = "password_expired_date")
    private Timestamp expireDate;

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

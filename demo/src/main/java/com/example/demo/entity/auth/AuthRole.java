package com.example.demo.entity.auth;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@Table(name = "auth_role")
@ToString
@NoArgsConstructor
public class AuthRole {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private int id;

    @Basic
    @Column(name = "name")
    private String name;

    @ManyToMany(mappedBy = "roles")
    private Set<AuthUser> users = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "roles_permissions",
            joinColumns = @JoinColumn(
                    name = "role_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(
                    name = "permission_id", referencedColumnName = "id"))
    private Set<AuthPermission> permissions = new HashSet<>();



    public void addAuthPermission(AuthPermission permission) {
        permissions.add(permission);
        permission.getRoles().add(this);
    }

    public void clearPermissions() {
        permissions.forEach(permission -> {
            permission.getRoles().remove(this);
        });
        permissions.clear();
    }


}

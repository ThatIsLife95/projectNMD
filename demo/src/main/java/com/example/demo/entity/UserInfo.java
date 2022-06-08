package com.example.demo.entity;

import com.example.demo.entity.auth.AuthUser;
import com.example.demo.enums.EGender;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "user_info")
@ToString
@NoArgsConstructor
public class UserInfo {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private int id;

    @Basic
    @Column(name = "display_name")
    private String displayName;

    @Basic
    @Column(name = "cover_image")
    private String coverImage;

    @Basic
    @Column(name = "avatar_image")
    private String avatarImage;

    @Basic
    @Column(name = "description")
    private String description;

    @Basic
    @Column(name = "date_of_birth")
    private String dateOfBirth;

    @Basic
    @Column(name = "gender")
    private EGender gender;

    @Basic
    @Column(name = "id_number")
    private String idNumber;

    @Basic
    @Column(name = "phone_number")
    private String phoneNumber;

    @Basic
    @Column(name = "address")
    private String address;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "auth_user_id", referencedColumnName = "id")
    private AuthUser authUser;
}

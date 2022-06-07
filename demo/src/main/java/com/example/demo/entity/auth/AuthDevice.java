package com.example.demo.entity.auth;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "auth_device")
@ToString
@NoArgsConstructor
public class AuthDevice {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private int id;
    @Basic
    @Column(name = "device_location")
    private String deviceLocation;

    @Basic
    @Column(name = "device_details")
    private String deviceDetails;

    @Basic
    @Column(name = "status")
    private boolean status;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private AuthUser user;

    public AuthDevice(String deviceLocation, String deviceDetails, boolean status) {
        this.deviceLocation = deviceLocation;
        this.deviceDetails = deviceDetails;
        this.status = status;
    }
}

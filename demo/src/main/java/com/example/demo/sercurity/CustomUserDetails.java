package com.example.demo.sercurity;

import com.example.demo.entity.auth.AuthDevice;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
public class CustomUserDetails implements UserDetails {
    private int id;
    private String username;
    private String email;
    private String password;
    private List<SimpleGrantedAuthority> authorities;
    private boolean status;
    private LocalDateTime expireDate;
    private Set<AuthDevice> deviceInfos = new HashSet<>();

    public CustomUserDetails(int id, String username,String email, String password, List<SimpleGrantedAuthority> authorities, boolean status, LocalDateTime expireDate, Set<AuthDevice> deviceInfos) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.authorities = authorities;
        this.status = status;
        this.expireDate = expireDate;
        this.deviceInfos = deviceInfos;
    }

    public boolean isNewDevice(String deviceLocation, String deviceDetails) {
        for (AuthDevice device : deviceInfos) {
            if (device.getDeviceLocation().equals(deviceLocation) && device.getDeviceDetails().equals(deviceDetails)
                    && device.isStatus()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        if (LocalDateTime.now().isBefore(expireDate)) {
            return true;
        }
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return status;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}

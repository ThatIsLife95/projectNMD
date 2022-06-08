package com.example.demo.dto;

import com.example.demo.enums.EGender;
import lombok.Data;

@Data
public class UserInfoDto {
    private String displayName;
    private String coverImage;
    private String avatarImage;
    private String description;
    private String dateOfBirth;
    private EGender gender;
    private String idNumber;
    private String phoneNumber;
    private String address;

}

package com.example.demo.dto;

import com.example.demo.entity.auth.EActionName;
import lombok.Data;

@Data
public class UserHistoryDto {
    private String username;
    private String displayName;
    private String email;
    private String password;
    private EActionName actionName;
    private boolean actionStatus;
}

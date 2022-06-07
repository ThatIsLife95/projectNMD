package com.example.demo.dto;

import lombok.Data;

import java.util.List;

@Data
public class RoleDto {
    private int id;
    private String name;
    private List<PermissionDto> permissions;
}

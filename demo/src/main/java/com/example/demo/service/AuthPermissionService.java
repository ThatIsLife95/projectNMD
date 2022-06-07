package com.example.demo.service;


import com.example.demo.dto.PermissionDto;
import com.example.demo.dto.ResponseDto;

public interface AuthPermissionService {
    public ResponseDto<?> getPermissions();

    public ResponseDto<?> getPermission(Integer id);

    public ResponseDto<?> createPermission(PermissionDto permissionDto);

    public ResponseDto<?> updatePermission(Integer id, PermissionDto permissionDto);

    public ResponseDto<?> deletePermission(Integer id);
}

package com.example.demo.service;


import com.example.demo.payload.PermissionDto;
import com.example.demo.payload.response.ResponseEntity;

public interface AuthPermissionService {
    public ResponseEntity<?> getPermissions();

    public ResponseEntity<?> getPermission(Integer id);

    public ResponseEntity<?> createPermission(PermissionDto permissionDto);

    public ResponseEntity<?> updatePermission(Integer id, PermissionDto permissionDto);

    public ResponseEntity<?> deletePermission(Integer id);
}

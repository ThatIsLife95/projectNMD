package com.example.demo.service;


import com.example.demo.payload.response.ResponseEntity;
import com.example.demo.payload.RoleDto;

public interface AuthRoleService {
    public ResponseEntity<?> getRoles();

    public ResponseEntity<?> getRole(Integer id);

    public ResponseEntity<?> createRole(RoleDto roleDto);

    public ResponseEntity<?> updateRole(Integer id, RoleDto roleDto);

    public ResponseEntity<?> deleteRole(Integer id);
}

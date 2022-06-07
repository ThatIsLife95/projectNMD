package com.example.demo.service.auth;


import com.example.demo.dto.ResponseDto;
import com.example.demo.dto.RoleDto;

public interface AuthRoleService {
    public ResponseDto<?> getRoles();

    public ResponseDto<?> getRole(Integer id);

    public ResponseDto<?> createRole(RoleDto roleDto);

    public ResponseDto<?> updateRole(Integer id, RoleDto roleDto);

    public ResponseDto<?> deleteRole(Integer id);
}

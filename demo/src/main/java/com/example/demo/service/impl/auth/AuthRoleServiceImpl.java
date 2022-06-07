package com.example.demo.service.impl.auth;


import com.example.demo.constants.HttpStatusConstants;
import com.example.demo.dto.PermissionDto;
import com.example.demo.dto.ResponseDto;
import com.example.demo.dto.RoleDto;
import com.example.demo.entity.auth.AuthPermission;
import com.example.demo.entity.auth.AuthRole;
import com.example.demo.exceptions.BusinessException;
import com.example.demo.repository.AuthPermissionRepository;
import com.example.demo.repository.AuthRoleRepository;
import com.example.demo.service.auth.AuthRoleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class AuthRoleServiceImpl implements AuthRoleService {

    private final AuthRoleRepository roleRepository;

    private final AuthPermissionRepository permissionRepository;

    private final ModelMapper modelMapper;

    @Override
    public ResponseDto<?> getRoles() {
        List<AuthRole> authRoles = roleRepository.findAll();
        List<RoleDto> roles = authRoles.stream().map(authRole -> {
            RoleDto role = modelMapper.map(authRole, RoleDto.class);
            List<PermissionDto> permissions = authRole.getPermissions().stream().map(authPermission -> modelMapper.map(authPermission, PermissionDto.class)).collect(Collectors.toList());
            role.setPermissions(permissions);
            return role;
        }).collect(Collectors.toList());

        return ResponseDto.ok(roles);
    }

    @Override
    public ResponseDto<?> getRole(Integer id) {
        AuthRole authRole = roleRepository.findById(id).orElseThrow(
                () -> new BusinessException(HttpStatusConstants.ROLE_NOT_EXISTED_CODE, HttpStatusConstants.ROLE_NOT_EXISTED_MESSAGE));
        return ResponseDto.ok(modelMapper.map(authRole, RoleDto.class));
    }

    @Override
    public ResponseDto<?> createRole(RoleDto roleDto) {
        ResponseDto<?> responseDto = ResponseDto.error(HttpStatusConstants.ROLE_EXISTED_CODE, HttpStatusConstants.ROLE_EXISTED_MESSAGE);
        boolean isExistedRole = roleRepository.findByName(roleDto.getName()).isPresent();
        if (!isExistedRole) {
            AuthRole authRole = roleRepository.save(modelMapper.map(roleDto, AuthRole.class));
            responseDto = ResponseDto.ok(modelMapper.map(authRole, RoleDto.class));
        }
        return responseDto;
    }

    @Override
    public ResponseDto<?> updateRole(Integer id, RoleDto roleDto) {
        AuthRole authRole = roleRepository.findById(id).orElseThrow(
                () -> new BusinessException(HttpStatusConstants.ROLE_NOT_EXISTED_CODE, HttpStatusConstants.ROLE_NOT_EXISTED_MESSAGE)
        );
        authRole.setName(roleDto.getName());
        authRole.clearPermissions();
        roleDto.getPermissions().forEach(
                permissionDto -> {
                    AuthPermission permission = permissionRepository.findById(permissionDto.getId()).orElseThrow(
                            () -> new BusinessException(HttpStatusConstants.PERMISSION_NOT_EXISTED_CODE, HttpStatusConstants.PERMISSION_NOT_EXISTED_MESSAGE)
                    );
                    authRole.addAuthPermission(permission);
                }
        );
        AuthRole authRoleResponse = roleRepository.save(authRole);
        return ResponseDto.ok(modelMapper.map(authRoleResponse, RoleDto.class));
    }

    @Override
    public ResponseDto<?> deleteRole(Integer id) {
        roleRepository.deleteById(id);
        return ResponseDto.ok(id);
    }
}

package com.example.demo.service.impl;

import com.example.demo.constants.HttpStatusConstants;
import com.example.demo.payload.PermissionDto;
import com.example.demo.payload.response.ResponseEntity;
import com.example.demo.entity.auth.AuthPermission;
import com.example.demo.exceptions.BusinessException;
import com.example.demo.repository.AuthPermissionRepository;
import com.example.demo.service.AuthPermissionService;
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
public class AuthPermissionServiceImpl implements AuthPermissionService {

    private final AuthPermissionRepository permissionRepository;

    private final ModelMapper modelMapper;

    @Override
    public ResponseEntity<?> getPermissions() {
        List<AuthPermission> permissions = permissionRepository.findAll();
        return ResponseEntity.ok(
                permissions.stream().map(authPermission -> modelMapper.map(authPermission, PermissionDto.class)).collect(Collectors.toList())
        );
    }

    @Override
    public ResponseEntity<?> getPermission(Integer id) {
        AuthPermission authPermission = permissionRepository.findById(id).orElseThrow(
                () -> new BusinessException(HttpStatusConstants.PERMISSION_NOT_EXISTED_CODE, HttpStatusConstants.PERMISSION_NOT_EXISTED_MESSAGE));
        return ResponseEntity.ok(
                modelMapper.map(authPermission, PermissionDto.class)
        );
    }

    @Override
    public ResponseEntity<?> createPermission(PermissionDto permissionDto) {
        ResponseEntity<?> responseEntity = ResponseEntity.error(HttpStatusConstants.PERMISSION_EXISTED_CODE, HttpStatusConstants.PERMISSION_EXISTED_MESSAGE);
        boolean isExistedPermission = permissionRepository.findByName(permissionDto.getName()).isPresent();
        if (!isExistedPermission) {
            AuthPermission authPermission = permissionRepository.save(modelMapper.map(permissionDto, AuthPermission.class));
            responseEntity = ResponseEntity.ok(modelMapper.map(authPermission, PermissionDto.class));
        }
        return responseEntity;
    }

    @Override
    public ResponseEntity<?> updatePermission(Integer id, PermissionDto permissionDto) {
        AuthPermission authPermission = permissionRepository.findById(id).orElseThrow(
                () -> new BusinessException(HttpStatusConstants.PERMISSION_NOT_EXISTED_CODE, HttpStatusConstants.PERMISSION_NOT_EXISTED_MESSAGE)
        );
        authPermission.setName(permissionDto.getName());
        AuthPermission authPermissionResponse = permissionRepository.save(authPermission);
        return ResponseEntity.ok(
                modelMapper.map(authPermissionResponse, PermissionDto.class)
        );
    }

    @Override
    public ResponseEntity<?> deletePermission(Integer id) {
        permissionRepository.deleteById(id);
        return ResponseEntity.ok(id);
    }


}

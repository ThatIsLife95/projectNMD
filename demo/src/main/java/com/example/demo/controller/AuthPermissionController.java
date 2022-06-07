package com.example.demo.controller;


import com.example.demo.constants.UriConstants;
import com.example.demo.dto.PermissionDto;
import com.example.demo.dto.ResponseDto;
import com.example.demo.service.auth.AuthPermissionService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = UriConstants.PERMISSION_URI)
@AllArgsConstructor
@Slf4j
public class AuthPermissionController {

    private final AuthPermissionService permissionService;

    @GetMapping()
    public ResponseDto<?> getPermissions() {
        return permissionService.getPermissions();
    }

    @GetMapping("/{id}")
    public ResponseDto<?> getPermission(@PathVariable Integer id) {
        return permissionService.getPermission(id);
    }

    @PostMapping()
    public ResponseDto<?> createPermission(@RequestBody PermissionDto permissionDto) {
        return permissionService.createPermission(permissionDto);
    }

    @PutMapping("/{id}")
    public ResponseDto<?> updatePermission(@PathVariable Integer id, @RequestBody PermissionDto permissionDto) {
        return permissionService.updatePermission(id, permissionDto);
    }

    @DeleteMapping("/{id}")
    public ResponseDto<?> updatePermission(@PathVariable Integer id) {
        return permissionService.deletePermission(id);
    }
}

package com.example.demo.controller;


import com.example.demo.constants.UriConstants;
import com.example.demo.payload.PermissionDto;
import com.example.demo.payload.response.ResponseEntity;
import com.example.demo.service.AuthPermissionService;
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
    public ResponseEntity<?> getPermissions() {
        return permissionService.getPermissions();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getPermission(@PathVariable Integer id) {
        return permissionService.getPermission(id);
    }

    @PostMapping()
    public ResponseEntity<?> createPermission(@RequestBody PermissionDto permissionDto) {
        return permissionService.createPermission(permissionDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updatePermission(@PathVariable Integer id, @RequestBody PermissionDto permissionDto) {
        return permissionService.updatePermission(id, permissionDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> updatePermission(@PathVariable Integer id) {
        return permissionService.deletePermission(id);
    }
}

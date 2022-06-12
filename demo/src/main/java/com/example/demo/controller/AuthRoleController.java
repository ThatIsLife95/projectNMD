package com.example.demo.controller;


import com.example.demo.constants.UriConstants;
import com.example.demo.payload.response.ResponseEntity;
import com.example.demo.payload.RoleDto;
import com.example.demo.service.AuthRoleService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = UriConstants.ROLE_URI)
@AllArgsConstructor
@Slf4j
public class AuthRoleController {

    private final AuthRoleService roleService;

    @GetMapping()
    public ResponseEntity<?> getRoles() {
        return roleService.getRoles();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getRole(@PathVariable Integer id) {
        return roleService.getRole(id);
    }

    @PostMapping()
    public ResponseEntity<?> createRole(@RequestBody RoleDto roleDto) {
        return roleService.createRole(roleDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateRole(@PathVariable Integer id, @RequestBody RoleDto roleDto) {
        return roleService.updateRole(id, roleDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> updateRole(@PathVariable Integer id) {
        return roleService.deleteRole(id);
    }
}

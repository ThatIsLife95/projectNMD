package com.example.demo.controller;


import com.example.demo.constants.UriConstants;
import com.example.demo.dto.ResponseDto;
import com.example.demo.dto.RoleDto;
import com.example.demo.service.auth.AuthRoleService;
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
    public ResponseDto<?> getRoles() {
        return roleService.getRoles();
    }

    @GetMapping("/{id}")
    public ResponseDto<?> getRole(@PathVariable Integer id) {
        return roleService.getRole(id);
    }

    @PostMapping()
    public ResponseDto<?> createRole(@RequestBody RoleDto roleDto) {
        return roleService.createRole(roleDto);
    }

    @PutMapping("/{id}")
    public ResponseDto<?> updateRole(@PathVariable Integer id, @RequestBody RoleDto roleDto) {
        return roleService.updateRole(id, roleDto);
    }

    @DeleteMapping("/{id}")
    public ResponseDto<?> updateRole(@PathVariable Integer id) {
        return roleService.deleteRole(id);
    }
}

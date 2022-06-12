package com.example.demo.controller;

import com.example.demo.constants.UriConstants;
import com.example.demo.payload.request.PageRequest;
import com.example.demo.payload.request.ChangePasswordRequest;
import com.example.demo.payload.response.ResponseEntity;
import com.example.demo.payload.UserInfoDto;
import com.example.demo.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = UriConstants.USER_URI)
@AllArgsConstructor
@Slf4j
public class UserController {
    private final UserService userService;

    @GetMapping()
    @PreAuthorize("hasAuthority('USER_READ')")
    public ResponseEntity<?> getUsers(@RequestBody PageRequest pageRequest) {
        return userService.getUsers(pageRequest);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('USER_READ')")
    public ResponseEntity<?> getUser(@PathVariable Integer id) {
        return userService.getUser(id);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('USER_WRITE') or #id == authentication.principal.id")
    public ResponseEntity<?> updateUserInfo(@PathVariable Integer id, @RequestBody UserInfoDto userInfoDto) {
        return userService.updateUserInfo(id, userInfoDto);
    }

    @PutMapping("change-password/{id}")
    @PreAuthorize("hasAuthority('USER_WRITE') or #id == authentication.principal.id")
    public ResponseEntity<?> changePassword(@PathVariable Integer id, @RequestBody ChangePasswordRequest changePasswordRequest) {
        return userService.changePassword(id, changePasswordRequest);
    }

//    @DeleteMapping("/{id}")
//    @PreAuthorize("hasAuthority('USER_DELETE')")
//    public BibResponse<?> deleteUser(@PathVariable Integer id) {
//        return userService.deleteUser(id);
//    }

}

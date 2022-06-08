package com.example.demo.controller;

import com.example.demo.constants.UriConstants;
import com.example.demo.dto.PageRequestDto;
import com.example.demo.dto.PasswordDto;
import com.example.demo.dto.ResponseDto;
import com.example.demo.dto.UserInfoDto;
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
    public ResponseDto<?> getUsers(@RequestBody PageRequestDto pageRequestDto) {
        return userService.getUsers(pageRequestDto);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('USER_READ')")
    public ResponseDto<?> getUser(@PathVariable Integer id) {
        return userService.getUser(id);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('USER_WRITE') or #id == authentication.principal.id")
    public ResponseDto<?> updateUserInfo(@PathVariable Integer id, @RequestBody UserInfoDto userInfoDto) {
        return userService.updateUserInfo(id, userInfoDto);
    }

    @PutMapping("change-password/{id}")
    @PreAuthorize("hasAuthority('USER_WRITE') or #id == authentication.principal.id")
    public ResponseDto<?> changePassword(@PathVariable Integer id, @RequestBody PasswordDto passwordDto) {
        return userService.changePassword(id, passwordDto);
    }

//    @DeleteMapping("/{id}")
//    @PreAuthorize("hasAuthority('USER_DELETE')")
//    public BibResponse<?> deleteUser(@PathVariable Integer id) {
//        return userService.deleteUser(id);
//    }

}

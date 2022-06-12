package com.example.demo.service;

import com.example.demo.payload.request.PageRequest;
import com.example.demo.payload.request.ChangePasswordRequest;
import com.example.demo.payload.response.ResponseEntity;
import com.example.demo.payload.UserInfoDto;

public interface UserService {
    public ResponseEntity<?> getUsers(PageRequest pageRequest);

    public ResponseEntity<?> getUser(Integer id);

    public ResponseEntity<?> updateUserInfo(Integer id, UserInfoDto userInfoDto);

    public ResponseEntity<?> changePassword(Integer id, ChangePasswordRequest changePasswordRequest);

}

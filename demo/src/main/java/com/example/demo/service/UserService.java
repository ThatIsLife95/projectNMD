package com.example.demo.service;

import com.example.demo.dto.PageRequestDto;
import com.example.demo.dto.PasswordDto;
import com.example.demo.dto.ResponseDto;
import com.example.demo.dto.UserInfoDto;

public interface UserService {
    public ResponseDto<?> getUsers(PageRequestDto pageRequestDto);

    public ResponseDto<?> getUser(Integer id);

    public ResponseDto<?> updateUserInfo(Integer id, UserInfoDto userInfoDto);

    public ResponseDto<?> changePassword(Integer id, PasswordDto passwordDto);

}

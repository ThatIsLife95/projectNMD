package com.example.demo.service.impl;

import com.example.demo.constants.DefaultConstants;
import com.example.demo.constants.HttpStatusConstants;
import com.example.demo.dto.*;
import com.example.demo.entity.UserInfo;
import com.example.demo.entity.auth.AuthUser;
import com.example.demo.exceptions.BusinessException;
import com.example.demo.repository.AuthUserRepository;
import com.example.demo.repository.UserInfoRepository;
import com.example.demo.service.UserService;
import com.example.demo.utils.Common;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final AuthUserRepository userRepository;

    private final UserInfoRepository userInfoRepository;

    private final ModelMapper modelMapper;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public ResponseDto<?> getUsers(PageRequestDto pageRequestDto) {
        String sortType = pageRequestDto.getSortType();
        // Chưa validate sortColumn có hợp lệ hay không
        String sortColumn = pageRequestDto.getSortColumn();
        Sort sortable = Sort.by(sortColumn).ascending();
        if (DefaultConstants.DESC_SORT_TYPE.equals(sortType)) {
            sortable = Sort.by(sortColumn).descending();
        }
        Pageable pageable = PageRequest.of(pageRequestDto.getPage(), pageRequestDto.getSize(), sortable);
        Specification<AuthUser> conditions = Common.getSpecifications(pageRequestDto.getFilters());
        Page<AuthUser> authUsersPage = userRepository.findAll(conditions, pageable);
        List<UserDto> users = authUsersPage.getContent().stream().map(authUser -> modelMapper.map(authUser, UserDto.class)).collect(Collectors.toList());
        Page<UserDto> usersPage = new PageImpl<>(users, authUsersPage.getPageable(), authUsersPage.getTotalElements());
        return ResponseDto.ok(usersPage);
    }

    @Override
    public ResponseDto<?> getUser(Integer id) {
        AuthUser authUser = userRepository.findById(id).orElseThrow(
                () -> new BusinessException(HttpStatusConstants.USERNAME_NOT_EXISTED_CODE, HttpStatusConstants.USERNAME_NOT_EXISTED_MESSAGE)
        );
        return ResponseDto.ok(modelMapper.map(authUser, UserDto.class));
    }

    @Override
    public ResponseDto<?> updateUserInfo(Integer id, UserInfoDto userInfoDto) {
        UserInfo userInfo = userInfoRepository.findByAuthUser_Id(id).orElseThrow(
                () -> new BusinessException(HttpStatusConstants.USERNAME_NOT_EXISTED_CODE, HttpStatusConstants.USERNAME_NOT_EXISTED_MESSAGE)
        );
        int id1 = userInfo.getId();
        modelMapper.map(userInfoDto, userInfo);
        userInfo.setId(id1);
        UserInfo userInfoResponse = userInfoRepository.save(userInfo);
        return ResponseDto.ok(modelMapper.map(userInfoResponse, UserInfoDto.class));
    }

    @Override
    public ResponseDto<?> changePassword(Integer id, PasswordDto passwordDto) {
        AuthUser authUser = userRepository.findById(id).orElseThrow(
                () -> new BusinessException(HttpStatusConstants.USERNAME_NOT_EXISTED_CODE, HttpStatusConstants.USERNAME_NOT_EXISTED_MESSAGE)
        );
        String oldPasswordHash = bCryptPasswordEncoder.encode(passwordDto.getOldPassword());
        if (authUser.getPassword().equals(oldPasswordHash)) {
            authUser.setPassword(bCryptPasswordEncoder.encode(passwordDto.getNewPassword()));
            userRepository.save(authUser);
            return ResponseDto.ok(null);
        }
        return ResponseDto.error(HttpStatusConstants.INVALID_OLD_PASSWORD_CODE, HttpStatusConstants.INVALID_OLD_PASSWORD_MESSAGE);
    }
}

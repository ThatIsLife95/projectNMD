package com.example.demo.service.impl;

import com.example.demo.constants.DefaultConstants;
import com.example.demo.constants.HttpStatusConstants;
import com.example.demo.payload.*;
import com.example.demo.entity.auth.AuthUser;
import com.example.demo.exceptions.BusinessException;
import com.example.demo.payload.request.PageRequest;
import com.example.demo.payload.request.ChangePasswordRequest;
import com.example.demo.payload.response.ResponseEntity;
import com.example.demo.repository.AuthUserRepository;
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

    private final ModelMapper modelMapper;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public ResponseEntity<?> getUsers(PageRequest pageRequest) {
        String sortType = pageRequest.getSortType();
        // Chưa validate sortColumn có hợp lệ hay không
        String sortColumn = pageRequest.getSortColumn();
        Sort sortable = Sort.by(sortColumn).ascending();
        if (DefaultConstants.DESC_SORT_TYPE.equals(sortType)) {
            sortable = Sort.by(sortColumn).descending();
        }
        Pageable pageable = org.springframework.data.domain.PageRequest.of(pageRequest.getPage(), pageRequest.getSize(), sortable);
        Specification<AuthUser> conditions = Common.getSpecifications(pageRequest.getFilters());
        Page<AuthUser> authUsersPage = userRepository.findAll(conditions, pageable);
        List<UserDto> users = authUsersPage.getContent().stream().map(authUser -> modelMapper.map(authUser, UserDto.class)).collect(Collectors.toList());
        Page<UserDto> usersPage = new PageImpl<>(users, authUsersPage.getPageable(), authUsersPage.getTotalElements());
        return ResponseEntity.ok(usersPage);
    }

    @Override
    public ResponseEntity<?> getUser(Integer id) {
        AuthUser authUser = userRepository.findById(id).orElseThrow(
                () -> new BusinessException(HttpStatusConstants.USERNAME_NOT_EXISTED_CODE, HttpStatusConstants.USERNAME_NOT_EXISTED_MESSAGE)
        );
        return ResponseEntity.ok(modelMapper.map(authUser, UserDto.class));
    }

    @Override
    public ResponseEntity<?> updateUserInfo(Integer id, UserInfoDto userInfoDto) {
//        UserInfo userInfo = userInfoRepository.findByAuthUser_Id(id).orElseThrow(
//                () -> new BusinessException(HttpStatusConstants.USERNAME_NOT_EXISTED_CODE, HttpStatusConstants.USERNAME_NOT_EXISTED_MESSAGE)
//        );
//        int id1 = userInfo.getId();
//        modelMapper.map(userInfoDto, userInfo);
//        userInfo.setId(id1);
//        UserInfo userInfoResponse = userInfoRepository.save(userInfo);
//        return ResponseEntity.ok(modelMapper.map(userInfoResponse, UserInfoDto.class));
        return null;
    }

    @Override
    public ResponseEntity<?> changePassword(Integer id, ChangePasswordRequest changePasswordRequest) {
        AuthUser authUser = userRepository.findById(id).orElseThrow(
                () -> new BusinessException(HttpStatusConstants.USERNAME_NOT_EXISTED_CODE, HttpStatusConstants.USERNAME_NOT_EXISTED_MESSAGE)
        );
        String oldPasswordHash = bCryptPasswordEncoder.encode(changePasswordRequest.getOldPassword());
        if (authUser.getPassword().equals(oldPasswordHash)) {
            authUser.setPassword(bCryptPasswordEncoder.encode(changePasswordRequest.getNewPassword()));
            userRepository.save(authUser);
            return ResponseEntity.ok(null);
        }
        return ResponseEntity.error(HttpStatusConstants.INVALID_OLD_PASSWORD_CODE, HttpStatusConstants.INVALID_OLD_PASSWORD_MESSAGE);
    }
}

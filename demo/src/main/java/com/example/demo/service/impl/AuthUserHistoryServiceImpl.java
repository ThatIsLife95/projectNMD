package com.example.demo.service.impl;

import com.example.demo.dto.UserHistoryDto;
import com.example.demo.entity.auth.AuthUserHistory;
import com.example.demo.repository.AuthUserHistoryRepository;
import com.example.demo.service.AuthUserHistoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class AuthUserHistoryServiceImpl implements AuthUserHistoryService {

    private final AuthUserHistoryRepository userHistoryRepository;

    private final ModelMapper modelMapper;

    @Override
    public void log(UserHistoryDto userHistoryDto) {
        userHistoryRepository.save(modelMapper.map(userHistoryDto, AuthUserHistory.class));
    }
}

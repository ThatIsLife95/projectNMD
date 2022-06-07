package com.example.demo.service;

import com.example.demo.dto.UserHistoryDto;
import com.example.demo.entity.auth.AuthUserHistory;

public interface AuthUserHistoryService {
    public void log(UserHistoryDto userHistoryDto);
}

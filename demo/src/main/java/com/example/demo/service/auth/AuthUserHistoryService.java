package com.example.demo.service.auth;

import com.example.demo.dto.UserHistoryDto;
import com.example.demo.entity.auth.AuthUserHistory;

public interface AuthUserHistoryService {
    public void log(UserHistoryDto userHistoryDto);
}

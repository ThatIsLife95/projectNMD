package com.example.demo.service;

import com.example.demo.enums.EActionName;

public interface AuthUserHistoryService {
    void log(String emailOrUsername, EActionName actionName, boolean actionStatus);
}

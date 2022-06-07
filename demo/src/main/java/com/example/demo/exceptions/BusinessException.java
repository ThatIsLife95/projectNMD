package com.example.demo.exceptions;

import lombok.*;

@AllArgsConstructor
public class BusinessException extends RuntimeException{
    private String code;
    private String message;
}

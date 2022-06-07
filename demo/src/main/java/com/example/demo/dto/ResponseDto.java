package com.example.demo.dto;

import com.example.demo.constants.HttpStatusConstants;
import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Data
@Builder
public class ResponseDto<T> {
    private String code;
    private String message;
    private T data;

    public static ResponseDto<?> ok(Object data) {
        return ResponseDto.builder().code(HttpStatusConstants.SUCCESS_CODE).message(HttpStatusConstants.SUCCESS_MESSAGE).data(data).build();
    }

    public static ResponseDto<?> error(String code, String message) {
        return ResponseDto.builder().code(code).message(message).build();
    }

    public static ResponseDto<?> buildAll(String code, String message, Object data) {
        return ResponseDto.builder().code(code).message(message).data(data).build();
    }
}

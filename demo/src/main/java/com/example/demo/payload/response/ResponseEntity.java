package com.example.demo.payload.response;

import com.example.demo.constants.HttpStatusConstants;
import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class ResponseEntity<T> {
    private String code;
    private String message;
    private T data;

    public static ResponseEntity<?> ok(Object data) {
        return ResponseEntity.builder().code(HttpStatusConstants.SUCCESS_CODE).message(HttpStatusConstants.SUCCESS_MESSAGE).data(data).build();
    }

    public static ResponseEntity<?> error(String code, String message) {
        return ResponseEntity.builder().code(code).message(message).build();
    }

    public static ResponseEntity<?> buildAll(String code, String message, Object data) {
        return ResponseEntity.builder().code(code).message(message).data(data).build();
    }
}

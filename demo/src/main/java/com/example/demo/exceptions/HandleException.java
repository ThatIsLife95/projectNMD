package com.example.demo.exceptions;

import com.example.demo.constants.HttpStatusConstants;
import com.example.demo.dto.ResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
@ResponseBody
@RequiredArgsConstructor
@Slf4j
public class HandleException {

    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseDto<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.error("MethodArgumentNotValidException => rootCause: {}", Arrays.stream(e.getStackTrace()).findFirst());
        log.error("MethodArgumentNotValidException => localizedMessage: {}", e.getMessage());
        Map<String, String> errors = new HashMap<>();
        e.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return ResponseDto.buildAll(HttpStatusConstants.INVALID_FIELD_CODE, HttpStatusConstants.INVALID_FIELD_MESSAGE, errors);
    }
}

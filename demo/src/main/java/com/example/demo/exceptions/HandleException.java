package com.example.demo.exceptions;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.example.demo.constants.HttpStatusConstants;
import com.example.demo.payload.response.ResponseEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
@ResponseBody
@RequiredArgsConstructor
@Slf4j
public class HandleException {

    @ExceptionHandler({IllegalStateException.class})
    public ResponseEntity<?> handleIllegalException(IllegalStateException e) {
        log.error("IllegalStateException => rootCause: {}", Arrays.stream(e.getStackTrace()).findFirst());
        log.error("IllegalStateException => localizedMessage: {}", e.getMessage());
        return ResponseEntity.error(HttpStatusConstants.NULL_POINTER_OR_BAD_REQUEST_CODE, HttpStatusConstants.NULL_POINTER_OR_BAD_REQUEST_MESSAGE);
    }

    @ExceptionHandler({NullPointerException.class})
    public ResponseEntity<?> handleNullPointerException(NullPointerException e) {
        log.error("NullPointerException => rootCause: {}", Arrays.stream(e.getStackTrace()).findFirst());
        log.error("NullPointerException => localizedMessage: {}", e.getMessage());
        return ResponseEntity.error(HttpStatusConstants.NULL_POINTER_OR_BAD_REQUEST_CODE, HttpStatusConstants.NULL_POINTER_OR_BAD_REQUEST_MESSAGE);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleException(Exception e) {
        log.error("Exception => rootCause: {}", Arrays.stream(e.getStackTrace()).findFirst());
        log.error("Exception => Cause: {}", e.getMessage());
        return ResponseEntity.error(HttpStatusConstants.UNAVAILABLE_CODE, HttpStatusConstants.UNAVAILABLE_MESSAGE);
    }

    @ExceptionHandler(SQLException.class)
    public ResponseEntity<?> handleSQLException(SQLException e) {
            log.error("SQLException => rootCause: {}", Arrays.stream(e.getStackTrace()).findFirst());
            log.error("SQLException => Cause: {}", e.getMessage());
        return ResponseEntity.error(HttpStatusConstants.SQL_CONNECTION_ERROR_CODE, HttpStatusConstants.SQL_CONNECTION_ERROR_MESSAGE);
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.error("MethodArgumentNotValidException => rootCause: {}", Arrays.stream(e.getStackTrace()).findFirst());
        log.error("MethodArgumentNotValidException => localizedMessage: {}", e.getMessage());
        Map<String, String> errors = new HashMap<>();
        e.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return ResponseEntity.buildAll(HttpStatusConstants.INVALID_FIELD_CODE, HttpStatusConstants.INVALID_FIELD_MESSAGE, errors);
    }

    @ExceptionHandler({MailException.class})
    public ResponseEntity<?> handleMailException(MailException e) {
        log.error("MailException => rootCause: {}", Arrays.stream(e.getStackTrace()).findFirst());
        log.error("MailException => localizedMessage: {}", e.getMessage());
        return ResponseEntity.error(HttpStatusConstants.CANNOT_SEND_EMAIL_CODE, HttpStatusConstants.CANNOT_SEND_EMAIL_MESSAGE);
    }

    @ExceptionHandler({JWTVerificationException.class})
    public ResponseEntity<?> handleJWTVerificationException(JWTVerificationException e) {
        log.error("JWTVerificationException => rootCause: {}", Arrays.stream(e.getStackTrace()).findFirst());
        log.error("JWTVerificationException => localizedMessage: {}", e.getMessage());
        return ResponseEntity.error(HttpStatusConstants.INVALID_JWT_CODE, HttpStatusConstants.INVALID_JWT_MESSAGE);
    }
}

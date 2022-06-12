package com.example.demo.payload.request;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Data
public class EmailRequest {
    @NotNull
    @Email
    private String email;
}

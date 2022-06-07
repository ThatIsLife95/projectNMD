package com.example.demo.dto;

import com.example.demo.constants.RegexConstant;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
public class RegistrationDto {
    @Pattern(regexp = RegexConstant.USERNAME_REGEX, message = "Tên tài khoản chỉ được bao gồm A-Z, a-z, 0-9 hoặc _")
    @NotNull(message = "Tên tài khoản là bắt buộc")
    private String username;

    private String displayName;

    @Email
    @NotNull(message = "Email là bắt buộc")
    private String email;

    @Pattern(regexp = RegexConstant.PASSWORD_REGEX, message = "Mật khẩu phải chứa chữ cái, số và kí tự đặc biệt, viết thường và viết hoa")
    @NotNull(message = "Mật khẩu là bắt buộc")
    private String password;

    private String token;
}

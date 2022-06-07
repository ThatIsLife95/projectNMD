package com.example.demo.constants;

public class RegexConstant {
     public static final String PASSWORD_REGEX = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()–[{}]:;',?/*~$^+=<>]).{8,20}$";
     public static final String USERNAME_REGEX = "^[A-Za-z0-9_]+$";
}

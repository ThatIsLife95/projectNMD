package com.example.demo.dto;

import com.example.demo.enums.ESearchOperation;
import lombok.Data;

@Data
public class FilterDto {
    private String key;
    private ESearchOperation operation;
    private String value;
}

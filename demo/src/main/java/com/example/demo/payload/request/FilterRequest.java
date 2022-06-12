package com.example.demo.payload.request;

import com.example.demo.enums.ESearchOperation;
import lombok.Data;

@Data
public class FilterRequest {
    private String key;
    private ESearchOperation operation;
    private String value;
}

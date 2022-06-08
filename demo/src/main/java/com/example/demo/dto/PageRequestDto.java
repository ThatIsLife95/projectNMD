package com.example.demo.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class PageRequestDto {

    private Integer page;

    private Integer size;

    private List<FilterDto> filters = new ArrayList<>();

    private String sortColumn;

    private String sortType;
}

package com.example.demo.payload.request;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class PageRequest {

    private Integer page;

    private Integer size;

    private List<FilterRequest> filters = new ArrayList<>();

    private String sortColumn;

    private String sortType;
}

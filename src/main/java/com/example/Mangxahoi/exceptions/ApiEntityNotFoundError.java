package com.example.Mangxahoi.exceptions;

import lombok.Getter;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

@Getter
public class ApiEntityNotFoundError implements ApiSubError, Serializable  {

    private final String errorMessage;
//    private final String value;

    public ApiEntityNotFoundError(String className, String field, Object value) {
        this.errorMessage = String.format("Not found field: %s with value: %s", field, value);
    }


}

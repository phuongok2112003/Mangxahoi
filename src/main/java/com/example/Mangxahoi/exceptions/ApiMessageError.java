package com.example.Mangxahoi.exceptions;

import lombok.Getter;

import java.io.Serializable;

@Getter
public class ApiMessageError implements ApiSubError, Serializable {
    private final String errorMessage;
    private final String value;

    public ApiMessageError(String errorMessage, String value) {
        this.errorMessage = errorMessage;
        this.value = value;
    }

}

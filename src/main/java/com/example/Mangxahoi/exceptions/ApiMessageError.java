package com.example.Mangxahoi.exceptions;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;

import java.io.Serializable;
@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
public class ApiMessageError implements ApiSubError, Serializable {
    private final String errorMessage;


    public ApiMessageError(String errorMessage) {
        this.errorMessage = errorMessage;

    }

}

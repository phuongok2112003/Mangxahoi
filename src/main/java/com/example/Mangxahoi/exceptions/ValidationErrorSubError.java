package com.example.Mangxahoi.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ValidationErrorSubError implements ApiSubError {
    private final String errorMessage;
//    private final String value;
}

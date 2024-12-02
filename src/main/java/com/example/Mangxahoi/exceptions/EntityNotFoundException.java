package com.example.Mangxahoi.exceptions;

import lombok.Getter;
import org.springframework.util.ObjectUtils;

import java.util.List;

@Getter
public class EntityNotFoundException extends EOException {

    private final ApiEntityNotFoundError apiSubErrors;

    public EntityNotFoundException(String className, String field, Object value) {
        super(404001, "errors.not_found_entity", (String) value, new Object[0]);
        this.apiSubErrors = new ApiEntityNotFoundError(className, field, value);
    }

}

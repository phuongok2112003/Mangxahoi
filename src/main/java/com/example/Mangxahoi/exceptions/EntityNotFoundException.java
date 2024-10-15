package com.example.Mangxahoi.exceptions;

import lombok.Getter;
import org.springframework.util.ObjectUtils;

import java.util.List;

@Getter
public class EntityNotFoundException extends EOException {

    private final ApiEntityNotFoundError apiSubErrors;

    public EntityNotFoundException(String className, String field, Object value) {
        super(404001, "errors.not_found", (String) value, new Object[0]);
        this.apiSubErrors = new ApiEntityNotFoundError(className, field, value);
    }

    public EntityNotFoundException(Class<?> clazz, FieldValue fieldValue) {
        super(404001, "errors.not_found", null, new Object[0]);
        this.apiSubErrors = new ApiEntityNotFoundError(clazz.getName(), fieldValue);
    }

    public EntityNotFoundException(Class<?> clazz, List<FieldValue> fieldValues) {
        super(404001, "errors.not_found", null, new Object[0]);
        this.apiSubErrors = new ApiEntityNotFoundError(clazz.getName(), fieldValues);
    }

    public EntityNotFoundException(ApiEntityNotFoundError apiEntityNotFoundError) {
        super(404001, "errors.not_found", null, new Object[0]);
        this.apiSubErrors = apiEntityNotFoundError;
    }

    public EntityNotFoundException(String message) {
        super(404001, ObjectUtils.isEmpty(message) ? "errors.not_found" : message, null, new Object[0]);
        this.apiSubErrors = null;
    }

}

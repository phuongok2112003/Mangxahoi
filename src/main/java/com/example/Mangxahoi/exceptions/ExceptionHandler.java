package com.example.Mangxahoi.exceptions;

import com.example.Mangxahoi.utils.EOResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;

import java.nio.file.AccessDeniedException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@ControllerAdvice
public class ExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(ExceptionHandler.class);
    public ExceptionHandler() {
    }

    @org.springframework.web.bind.annotation.ExceptionHandler({EOException.class})
    protected ResponseEntity<Object> handleException(EOException ex) {
        log.error("Handle Exception. code = {}, message = {}", ex.code, ex.getMessage());
        ApiMessageError error = new ApiMessageError(ex.getMessage(), ex.value);
        return ResponseEntity.ok(EOResponse.buildMsg(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.message, error));
    }

    @org.springframework.web.bind.annotation.ExceptionHandler({Exception.class})
    protected ResponseEntity<Object> handleException(Exception ex) {
        log.error("Handle Exception: errorMessage = {}", ex.getMessage(), ex);
        ApiMessageError error = new ApiMessageError(ex.getMessage(), null);
        return ResponseEntity.ok(EOResponse.build(HttpStatus.INTERNAL_SERVER_ERROR.value(), "errors.internal_server_error", error, new Object[0]));
    }

    @org.springframework.web.bind.annotation.ExceptionHandler({EntityNotFoundException.class})
    protected ResponseEntity<Object> handleEntityNotFound(EntityNotFoundException ex) {
        ApiEntityNotFoundError subError = ex.getApiSubErrors();
        String fieldValuesStr = (String) subError.getFieldValues().stream().map(Objects::toString).collect(Collectors.joining(", "));
        log.error("Handle EntityNotFoundException. errorCode = {}, errorMessage = {}, className = {}. FieldValues: {}",
                new Object[]{ex.code, ex.getMessage(), subError.getClassName(), fieldValuesStr});
        return ResponseEntity.ok(EOResponse.buildMsg(ex.code, ex.message, ex.getApiSubErrors()));
    }
    @org.springframework.web.bind.annotation.ExceptionHandler({AccessDeniedException.class})
    protected ResponseEntity<Object> handleEntityNotFound(AccessDeniedException ex) {
        log.error("Handle Exception: errorMessage = {}", ex.getMessage(), ex);
        ApiMessageError error = new ApiMessageError(ex.getMessage(), null);
        return ResponseEntity.ok(EOResponse.build(HttpStatus.INTERNAL_SERVER_ERROR.value(), "errors.access is denied", error, new Object[0]));
    }
    @org.springframework.web.bind.annotation.ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
        );
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }
}

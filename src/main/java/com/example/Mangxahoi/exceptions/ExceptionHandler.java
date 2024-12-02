package com.example.Mangxahoi.exceptions;

import com.example.Mangxahoi.error.CommonStatus;
import com.example.Mangxahoi.utils.EOResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@ControllerAdvice
public class ExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(ExceptionHandler.class);

    @org.springframework.web.bind.annotation.ExceptionHandler({EOException.class})
    protected ResponseEntity<Object> handleException(EOException ex) {
        log.error("Handle Exception. code = {}, message = {}", ex.code, ex.getMessage());
        ApiMessageError error = new ApiMessageError(ex.getMessage(), ex.value);
        return ResponseEntity.status(500).body(EOResponse.buildMsg(ex.code, ex.message, error));
    }

    @org.springframework.web.bind.annotation.ExceptionHandler({Exception.class})
    protected ResponseEntity<Object> handleException(Exception ex) {
        log.error("Handle Exception: errorMessage = {}", ex.getMessage(), ex);
        ApiMessageError error = new ApiMessageError(ex.getMessage(), null);
        return ResponseEntity.status(500).body(EOResponse.build( "errors.internal_server_error", error, new Object[0]));
    }

    @org.springframework.web.bind.annotation.ExceptionHandler({EntityNotFoundException.class})
    protected ResponseEntity<Object> handleEntityNotFound(EntityNotFoundException ex) {
        ApiEntityNotFoundError subError = ex.getApiSubErrors();
        String fieldValuesStr = subError.getValue();
        log.error("Handle EntityNotFoundException. errorCode = {}, errorMessage = {}, className = {}. FieldValues: {}",
                new Object[]{ex.code, ex.getMessage(), subError.getErrorMessage(), fieldValuesStr});
        return ResponseEntity.status(400).body(EOResponse.buildMsg(ex.code, ex.message, subError));
    }
    @org.springframework.web.bind.annotation.ExceptionHandler({AccessDeniedException.class})
    protected ResponseEntity<Object> handleEntityNotFound(AccessDeniedException ex) {
        log.error("Handle Exception: errorMessage = {}", ex.getMessage(), ex);
        ApiMessageError error = new ApiMessageError(ex.getMessage(), null);
        return ResponseEntity.status(403).body(EOResponse.buildMsg(403,CommonStatus.FORBIDDEN.getMessage(),error));
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<EOResponse<List<ApiSubError>>> handleValidationExceptions(MethodArgumentNotValidException ex) {

        List<ApiSubError> validationErrors = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> new ValidationErrorSubError(error.getField(), error.getDefaultMessage()))
                .collect(Collectors.toList());

        EOResponse<List<ApiSubError>> response = EOResponse.buildMsg(
                HttpStatus.BAD_REQUEST.value(),
                "Validation Failed",
                validationErrors
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);

    }
}

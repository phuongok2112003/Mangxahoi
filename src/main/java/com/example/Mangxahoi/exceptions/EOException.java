package com.example.Mangxahoi.exceptions;

import com.example.Mangxahoi.error.ErrorStatus;
import com.example.Mangxahoi.utils.EbsI18n;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.lang.Nullable;
@EqualsAndHashCode(callSuper = true)
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EOException extends RuntimeException{
    protected  int code;
    protected  String message;
    protected  String value;


    public EOException(HttpStatus httpStatus, String messageUrl, @Nullable String value, Object... arg) {
        this.code = httpStatus.value();
        this.value = value;
        this.message = EbsI18n.get(messageUrl, arg);
    }

    public EOException(int code, String messageUrl, @Nullable String value, Object... arg) {
        this.code = code;
        this.value = value;
        this.message = EbsI18n.get(messageUrl, arg);
    }

    public EOException(ErrorStatus enumError, Object... arg) {
        this.code = enumError.getCode();
        this.value = null;
        this.message = EbsI18n.get(enumError.getMessage(), arg);
    }
}

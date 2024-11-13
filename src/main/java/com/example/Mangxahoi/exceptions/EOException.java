package com.example.Mangxahoi.exceptions;

import com.example.Mangxahoi.error.ErrorStatus;
import com.example.Mangxahoi.utils.EbsI18n;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.http.HttpStatus;
import org.springframework.lang.Nullable;
@EqualsAndHashCode(callSuper = true)
@Data
public class EOException extends RuntimeException{
    protected final int code;
    protected final String message;
    protected final String value;


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

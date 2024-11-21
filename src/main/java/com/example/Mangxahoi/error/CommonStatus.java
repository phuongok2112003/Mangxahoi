package com.example.Mangxahoi.error;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum CommonStatus implements ErrorStatus {
    SUCCESS(200, "messages.success"),
    FORBIDDEN(403, "errors.access_denied"),
    TokenIsInvalid(401,"Invalid token"),
    TokenExpired(401,"errors.The Token has expired"),
    OtpExpired(401,"errors.The Otp has expired"),
    ACCOUNT_NOT_FOUND(400, "errors.account_not_found"),
    ACCOUNT_LOG_TOO(400, "errors.Your account has logged in too many times"),
    PASSWORD_NOT_CONFIRM(400,"errors.password_not_confirm"),
    CANNOT_SEND_EMAIL(400, "errors.cant_sent_email"),

    ;

    private final int code;
    private final String message;

    @Override
    public int getCode() {
        return this.code;
    }

    @Override
    public String getMessage() {
        return this.message;
    }
}

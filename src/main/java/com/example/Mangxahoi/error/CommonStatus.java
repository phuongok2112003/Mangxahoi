package com.example.Mangxahoi.error;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum CommonStatus implements ErrorStatus {
    SUCCESS(200, "messages.success"),
    FORBIDDEN(403, "errors.access_denied"),
    TokenIsInvalid(401,"errors.invalid token"),
    TokenExpired(401,"errors.the Token has expired"),
    OtpExpired(401,"errors.the Otp has expired"),
    ACCOUNT_NOT_FOUND(400, "errors.account_not_found"),
    ACCOUNT_NOT_OTP(400, "errors.Not otp"),
    PASSWORD_NOT_CONFIRM(400,"errors.password_not_confirm"),
    CANNOT_SEND_EMAIL(400, "errors.cant_sent_email"),
     FAILURE(400,"Failure"),

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

package com.example.Mangxahoi.error;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum CommonStatus implements ErrorStatus {
    SUCCESS(200, "messages.success"),
    FORBIDDEN(403, "errors.access_denied"),
    TokenIsInvalid(401,"errors.invalid_token"),
    TokenExpired(401,"errors.the_token_has_expired"),
    OtpExpired(401,"errors.the_otp_has_expired"),
    ACCOUNT_NOT_FOUND(400, "errors.account_wrong_email_or_password"),
    ACCOUNT_NOT_OTP(400, "errors.not_otp"),
    PASSWORD_NOT_CONFIRM(400,"errors.password_not_confirm"),
    CANNOT_SEND_EMAIL(400, "errors.cant_sent_email"),
    CANNOT_LIKE_POST(400, "errors.you_liked_this_post"),
     FAILURE(400,"Failure"),
    CHECK_POST(400,"errors.content_and_images_is_empty"),
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

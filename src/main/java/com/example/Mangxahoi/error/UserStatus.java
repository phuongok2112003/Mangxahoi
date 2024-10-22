package com.example.Mangxahoi.error;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum UserStatus implements ErrorStatus {
    TOKEN_IS_EXPIRED(400, "errors.token_is_expired"),
    IS_NOT_TOKEN(400, "errors.this_is_not_token"),
    EMAIL_IS_WRONG_FORMAT(400, "errors.wrong_email_format"),
    CONFIRM_PASSWORD_IS_ERROR(400, "errors.confirm_password_is_error"),
    EMAIL_IS_EXIST(430_005, "errors.email_is_exist"),
    USERNAME_IS_EXIST(430_003, "errors.username_is_exist"),
    USERNAME_IS_EMPTY(400, "errors.username_is_empty"),
    EMAIL_IS_EMPTY(400, "errors.email_is_empty"),
    PASSWORD_IS_EMPTY(400, "errors.password_is_empty"),
    EMAIL_NOT_FOUND(400, "errors.email_not_found"),
  WRONG_OTP(400,"errors.user_wrong_otp"),
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

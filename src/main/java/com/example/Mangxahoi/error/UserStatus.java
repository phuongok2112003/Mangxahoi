package com.example.Mangxahoi.error;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum UserStatus implements ErrorStatus {

    EMAIL_IS_EXIST(400, "errors.email_is_exist"),
    SENDED_FRIEND(400, "errors.user_sended_friend"),
    USERNAME_IS_EMPTY(400, "errors.username_is_empty"),
    PASSWORD_IS_EMPTY(400, "errors.password_is_empty"),
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

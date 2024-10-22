package com.example.Mangxahoi.error;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum CommonStatus implements ErrorStatus {
    SUCCESS(200, "messages.success"),
    FORBIDDEN(403, "errors.access_denied"),
    TokenIsInvalid(401,"Invalid token"),
    TokenExpired(401,"errors.The Token has expired"),
    ACCOUNT_NOT_FOUND(400, "errors.account_not_found"),
    PASSWORD_NOT_CONFIRM(400,"errors.password_not_confirm"),
    TEMPORARY_LOCK_NOT_FINISH(400,"errors.the_temporary_lockout_period_is_not_over_yet"),
    ACCOUNT_IS_NOT_ACTIVATED(400, "errors.account_is_not_activated"),
    ACCOUNT_HAS_BEEN_LOCKED(400, "errors.account_has_been_locked"),
    WRONG_USERNAME_OR_PASSWORD(400, "errors.wrong_username_or_password"),
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

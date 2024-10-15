package com.example.Mangxahoi.error;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum CommonStatus implements ErrorStatus {
    SUCCESS(200, "messages.success"),
    FORBIDDEN(403, "errors.access_denied"),
    TokenIsInvalid(401,"Invalid token"),
    TokenExpired(401,"errors.The Token has expired"),
    NameExit(500,"Name bị trùng"),
    OBJECT_IS_A_FOREIGN_KEY(403, "errors.object_is_a_foreign_key"),

    INTERNAL_SERVER_ERROR(500, "errors.internal_server_error"),
    INPUT_OUTPUT_FILE_ERROR(500, "errors.io_file_error"),
    FIND_FILE_ERROR(500, "errors.find_file_error"),
    DATA_ACCESS_ERROR(500, "errors.data_access_error"),
    SQL_WRONG_FORMAT(500, "errors.data_access_error"),
    SERVER_SEND_MAIL_ERROR(500, "errors.server_send_mail_error"),

    NOT_FOUND(400, "errors.not_found"),
    WRONG_FORMAT(400, "errors.wrong_format"),
    ACCOUNT_NOT_FOUND(400, "errors.account_not_found"),
    TEMPORARY_LOCK(400,"errors.temporary_temporaryLock_%d_minutes"),
    TEMPORARY_LOCK_NOT_FINISH(400,"errors.the_temporary_lockout_period_is_not_over_yet"),
    ACCOUNT_IS_NOT_ACTIVATED(400, "errors.account_is_not_activated"),
    ACCOUNT_HAS_BEEN_LOCKED(400, "errors.account_has_been_locked"),
    WRONG_USERNAME_OR_PASSWORD(400, "errors.wrong_username_or_password"),
    OBJECT_ALREADY_EXIST(400, "errors.object_already_exist"),
    EMAIL_IS_WRONG_FORMAT(400, "errors.wrong_email_format"),
    EMAIL_IS_EXIST(400, "errors.email_is_exist"),
    SENT_EMAIL_TO_YOU(400, "errors.sent_2_emails_to_you"),

    CODE_SMS_IS_EMPTY(400, "errors.verify_code_is_empty"),
    CODE_SMS_INVALID(400, "errors.verify_code_invalid"),
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

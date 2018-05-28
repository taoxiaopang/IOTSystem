package io.qcheng.cloud.authentication.exception;

import io.qcheng.cloud.authentication.dto.User;

public class UserErrorType extends User {

    private String errorMsg;

    public UserErrorType(final String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public String getErrorMessage() {
        return errorMsg;
    }
}

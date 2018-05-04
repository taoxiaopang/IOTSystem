package io.qcheng.cloud.server.user.exception;

import io.qcheng.cloud.server.user.dto.UserDTO;

public class UserErrorType extends UserDTO {

    private String errorMsg;

    public UserErrorType(final String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public String getErrorMessage() {
        return errorMsg;
    }
}

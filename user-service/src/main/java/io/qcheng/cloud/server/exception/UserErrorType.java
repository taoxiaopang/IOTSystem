package io.qcheng.cloud.server.exception;

import io.qcheng.cloud.server.dto.UserDTO;

public class UserErrorType extends UserDTO {

    private String errorMsg;

    public UserErrorType(final String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public String getErrorMessage() {
        return errorMsg;
    }
}

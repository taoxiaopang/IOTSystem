package com.qcheng.cloud.server.exception;

import com.qcheng.cloud.server.dto.UserDTO;

public class UserErrorType extends UserDTO {

    private String errorMsg;

    public UserErrorType(final String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public String getErrorMessage() {
        return errorMsg;
    }
}

package io.qcheng.cloud.server.device.exception;

import io.qcheng.cloud.server.device.dto.MessageType;

public class MessageTypeErrorType extends MessageType {

	private String errorMsg;

	public MessageTypeErrorType(final String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public String getErrorMessage() {
		return errorMsg;
	}

}

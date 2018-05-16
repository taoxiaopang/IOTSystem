package io.qcheng.cloud.server.device.exception;

import io.qcheng.cloud.server.device.dto.DeviceType;

public class DeviceTypeErrorType extends DeviceType {

	private String errorMsg;

	public DeviceTypeErrorType(final String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public String getErrorMessage() {
		return errorMsg;
	}

}

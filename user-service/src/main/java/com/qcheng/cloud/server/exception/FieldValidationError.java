package com.qcheng.cloud.server.exception;

public class FieldValidationError {
	private String filed;
	private String message;

	public FieldValidationError(String filed, String message) {
		super();
		this.filed = filed;
		this.message = message;
	}

	public String getFiled() {
		return filed;
	}

	public void setFiled(String filed) {
		this.filed = filed;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}

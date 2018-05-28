package io.qcheng.cloud.authentication.dto;

import com.fasterxml.jackson.annotation.JsonValue;

public enum Role {

	USER("USER"), ADMIN("ADMIN");

	private String value;

	
	@JsonValue
	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value.toUpperCase();
	}

	Role(String value) {
		this.value = value;
	}

	public static Role fromString(String value) {
		for (Role r : Role.values()) {
			if (r.value.equalsIgnoreCase(value)) {
				return r;
			}
		}
		throw new IllegalArgumentException("No constant with value " + value + " found");
	}
}

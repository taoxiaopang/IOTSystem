package io.qcheng.cloud.server.device.utils;

import org.apache.commons.lang.RandomStringUtils;

public class DeviceServiceUtils {

	public static String generateIdentity() {

		int length = 20;
		boolean useLetters = true;
		boolean useNumbers = true;

		return RandomStringUtils.random(length, useLetters, useNumbers);
	}

}

package com.devicemon.project.shared;

public class FieldVerifier {

	// Verify Host Name
	public static boolean isValidHost(String host) {
		if (host == null) {
			return false;
		}
		return host.length() > 2;
	}

	// Verify Device Name
	public static boolean isValidDevice(String device) {
		if (device == null) {
			return false;
		}
		return device.length() > 2;
	}
}

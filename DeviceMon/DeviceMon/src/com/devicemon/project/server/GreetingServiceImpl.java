package com.devicemon.project.server;

import com.devicemon.project.client.GreetingService;
import com.devicemon.project.shared.FieldVerifier;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

//Server Code
@SuppressWarnings("serial")
public class GreetingServiceImpl extends RemoteServiceServlet implements
		GreetingService {

	public String greetServer(String hostName, String deviceName) throws IllegalArgumentException {
		// Validating Input 
		if (!FieldVerifier.isValidHost(hostName) || !FieldVerifier.isValidDevice(deviceName)) {
			// Exception if input is not valid.
			throw new IllegalArgumentException(
					"Please specify value(s)");
		}

		// Escape data from the client to avoid cross-site script vulnerabilities.
		hostName = escapeHtml(hostName);
		deviceName = escapeHtml(deviceName);

		return "Hello, " + "(" + hostName + "," + deviceName + ")" + "!<br><br>registered!";
	}

	// Escape the data
	private String escapeHtml(String html) {
		if (html == null) {
			return null;
		}
		return html.replaceAll("&", "&amp;").replaceAll("<", "&lt;")
				.replaceAll(">", "&gt;");
	}
}

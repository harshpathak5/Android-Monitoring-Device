package com.devicemon.project.client;

import com.devicemon.project.shared.FieldVerifier;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class DeviceMon implements EntryPoint {
	/**
	 * The message displayed to the user when the server cannot be reached or
	 * returns an error.
	 */
private static final String SERVER_ERROR = "An error occurred while "
			+ "attempting to contact the server. Please check your network "
			+ "connection and try again.";

	/**
	 * Create a remote service proxy to talk to the server-side Greeting service.
	 */
	private final GreetingServiceAsync greetingService = GWT
			.create(GreetingService.class);

	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {

		// Error Label
		final Label errorLabel = new Label();
		errorLabel.setStyleName("errorLabel");

		// Create Parent Panel of all panels
		FlowPanel parentPanel = new FlowPanel();
	    parentPanel.setStyleName("parentPanel");

		// Header
		final Label header = new Label("Device Monitoring System");
		header.setStyleName("headerLabel");
		parentPanel.add(header);

		// Label info
		final Label askinfo = new Label("Enter Info");
		askinfo.setStyleName("insertLabel");

		// Button for adding device
		final Button deviceAdd = new Button("Add Device");
		deviceAdd.addStyleName("deviceAdd");
		
		// Host name text box
		final TextBox hostName = new TextBox();
		hostName.addStyleName("insertHost");

		// Device name text box
		final TextBox deviceName = new TextBox();
		deviceName.addStyleName("insertDevice");

		// Add flex Panel to display box for entering host name and device data and send it to server.
		FlexTable deviceData = new FlexTable();
		deviceData.setStyleName("displayData");
		// 1st Row
		deviceData.setWidget(0, 1, askinfo);
		// 2nd Row
		deviceData.setHTML(1, 0, "Host:");
		deviceData.setWidget(1, 1, hostName);
		// 3rd Row
		deviceData.setHTML(2, 0, "Device:");
		deviceData.setWidget(2, 1, deviceName);
		// 4th Row
		deviceData.setWidget(3, 1, deviceAdd);
		// 5th Row
		deviceData.setWidget(4, 0, errorLabel);
		deviceData.getFlexCellFormatter().setColSpan(4, 0, 3);

		// Create Panel for displaying flex table
		DecoratorPanel displayBox = new DecoratorPanel();
		displayBox.setStyleName("displayBox");
		displayBox.setWidget(deviceData);

		// Add decorator panel to parent panel
		parentPanel.add(displayBox);

		//Add parent panel to root panel.
		RootPanel.get("parentPanelContainer").add(parentPanel);

		// Create Dialog to print in case of failure
		final DialogBox dialogBox = new DialogBox();
		dialogBox.setText("Remote Procedure Call");
		dialogBox.setAnimationEnabled(true);
		final Button closeButton = new Button("Close");
		// We can set the id of a widget by accessing its Element
		closeButton.getElement().setId("closeButton");
		final Label textToServerLabel = new Label();
		final HTML serverResponseLabel = new HTML();
		VerticalPanel dialogVPanel = new VerticalPanel();
		dialogVPanel.addStyleName("dialogVPanel");
		dialogVPanel.add(new HTML("<b>Sending name to the server:</b>"));
		dialogVPanel.add(textToServerLabel);
		dialogVPanel.add(new HTML("<br><b>Server replies:</b>"));
		dialogVPanel.add(serverResponseLabel);
		dialogVPanel.setHorizontalAlignment(VerticalPanel.ALIGN_RIGHT);
		dialogVPanel.add(closeButton);
		dialogBox.setWidget(dialogVPanel);

		// Add a handler to close the DialogBox
		closeButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				dialogBox.hide();
				deviceAdd.setEnabled(true);
				deviceAdd.setFocus(true);
			}
		});

		// Handler for "Add Device Button"
		class MyHandler implements ClickHandler, KeyUpHandler {
			// User clicks add device button
			public void onClick(ClickEvent event) {
				sendInfoToServer();
			}

			public void onKeyUp(KeyUpEvent event) {
				if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
					sendInfoToServer();
				}
			}

			private void sendInfoToServer() {
				// Validating input.
				String hostInfo = hostName.getText();
				String deviceInfo = deviceName.getText();

				errorLabel.setText("");
				if (!(FieldVerifier.isValidHost(hostInfo)) || !(FieldVerifier.isValidDevice(deviceInfo))) {
					errorLabel.setText("Please enter valid Host/Device info!");
					deviceAdd.setEnabled(false);
					return;
				}

				// Then, we send the input to the server.
				deviceAdd.setEnabled(true);
				//textToServerLabel.setText(textToServer);
				//serverResponseLabel.setText("");
				 greetingService.greetServer(hostInfo, deviceInfo,
						new AsyncCallback<String>() {
							public void onFailure(Throwable caught) {
								return;
							}

							public void onSuccess(String result) {
								return;
							}
						});
			}
		}

		// Add a handler to send the name to the server
		MyHandler handler = new MyHandler();
		deviceAdd.addClickHandler(handler);
		hostName.addKeyUpHandler(handler);
		deviceName.addKeyUpHandler(handler);
	}

}

package io.azraein.aether.alerts;

import javafx.scene.control.Alert;

public class DisconnectedAlert extends Alert {

	public DisconnectedAlert() {
		super(AlertType.WARNING);

		this.setTitle("Disconnected from Aether Server");
		this.setHeaderText("Woah there! Slow down now buckaroo!");
		this.setContentText("Couldn't connect to the Aether Server");
	}

}

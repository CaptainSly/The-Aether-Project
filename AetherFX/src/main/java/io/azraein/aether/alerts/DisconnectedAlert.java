package io.azraein.aether.alerts;

import javafx.scene.control.Alert;

public class DisconnectedAlert extends Alert {

	public DisconnectedAlert() {
		super(AlertType.WARNING);

		this.setTitle("Disconnected from Aether Server");
		this.setHeaderText("Woah there! Slow down now buckaroo!");
		this.setContentText("We've been disconnected from the Server!");
	}

}

package io.azraein.aether.alerts;

import javafx.scene.control.Alert;

public class ServerErrorAlert extends Alert {

	public ServerErrorAlert(String message) {
		super(AlertType.ERROR);

		this.setTitle("Aether Server Error");
		this.setHeaderText("The Server seems to have thrown us an error!");
		this.setContentText(message);
	}

	public void setMessage(String msg) {
		this.setContentText(msg);
	}
	
}

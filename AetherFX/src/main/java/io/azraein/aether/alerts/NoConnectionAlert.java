package io.azraein.aether.alerts;

import javafx.scene.control.Alert;

public class NoConnectionAlert extends Alert {

	public NoConnectionAlert() {
		super(AlertType.ERROR);
		
		this.setTitle("No connection!");
		this.setHeaderText("We don't seem to be connected!");
		this.setContentText("It doesn't seem we're connected to the Aether Server!");
	}

}

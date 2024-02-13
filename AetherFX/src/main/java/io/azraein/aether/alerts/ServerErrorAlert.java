package io.azraein.aether.alerts;

import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;

public class ServerErrorAlert extends Alert {

	private TextArea errorArea;

	public ServerErrorAlert(String messageType, String message) {
		super(AlertType.ERROR);

		errorArea = new TextArea();
		errorArea.setEditable(false);
		errorArea.setWrapText(true);

		errorArea.setMaxWidth(Double.MAX_VALUE);
		errorArea.setMaxHeight(Double.MAX_VALUE);

		this.setTitle("Aether Server Error");
		this.setHeaderText("The Server seems to have thrown us an error!");
		this.setContentText(messageType);
		errorArea.setText(message);

		this.getDialogPane().setExpandableContent(errorArea);
	}

	public void setMessage(String messageType, String message) {
		this.setContentText(messageType);
		errorArea.setText(message);

	}

}

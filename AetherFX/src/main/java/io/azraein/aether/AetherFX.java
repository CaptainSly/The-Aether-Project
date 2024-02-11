package io.azraein.aether;

import java.net.Socket;
import java.util.HashMap;

import org.tinylog.Logger;

import io.azraein.aether.alerts.AboutAlert;
import io.azraein.aether.alerts.DisconnectedAlert;
import io.azraein.aether.alerts.NoConnectionAlert;
import io.azraein.aether.alerts.ServerErrorAlert;
import io.azraein.aether.screens.AetherLoginScreen;
import io.azraein.aether.screens.AetherScreen;
import io.azraein.aether.server.AetherClient;
import io.azraein.aether.utils.Aether;
import io.azraein.aether.utils.AetherConstants;
import io.azraein.aether.utils.AetherServerListenerServiceFX;
import javafx.application.Application;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

public class AetherFX extends Application {

	// AetherFX Client Stuff
	private AetherClient aetherClient;
	private AetherServerListenerServiceFX aetherSListener;

	private Stage primaryStage;

	private final HashMap<String, AetherScreen> aetherScreens = new HashMap<>();
	private final HashMap<String, Alert> aetherAlerts = new HashMap<>();

	private final BooleanProperty connectedToServerProperty = new SimpleBooleanProperty();
	private final StringProperty serverMessageProperty = new SimpleStringProperty();

	@Override
	public void start(Stage primaryStage) throws Exception {
		this.primaryStage = primaryStage;

		// Screens
		aetherScreens.put("loginScreen", new AetherLoginScreen(this));

		// Alerts
		aetherAlerts.put("aetherDisconnect", new DisconnectedAlert());
		aetherAlerts.put("aetherAbout", new AboutAlert());
		aetherAlerts.put("aetherNoConnection", new NoConnectionAlert());
		aetherAlerts.put("aetherServerError", new ServerErrorAlert(""));

		primaryStage.setScene(new Scene(aetherScreens.get("loginScreen").getRootContainer(), 1280, 720));
		primaryStage.setTitle("Aether E-Portal");
		primaryStage.show();

		connectedToServerProperty.addListener((obs, oldValue, newValue) -> {

			if (!newValue) {
				Logger.debug("Disconnected from Server");
				aetherAlerts.get("aetherDisconnect").showAndWait();
			}

			if (!newValue && aetherSListener != null) {
				aetherSListener.cancel(); // This will handle the cleanup
			}

			if (newValue && oldValue == false) {
				Logger.debug("Connecting to Server");
				aetherSListener = new AetherServerListenerServiceFX(this, aetherClient);
				aetherSListener.start();
			}

		});

		serverMessageProperty.addListener((obs, oldValue, newValue) -> {

			if (newValue != null) {
				Logger.debug("serverMessage == " + newValue);

				String[] tokens = newValue.split(" ");

				if (tokens[0].equals("error")) {
					// Merge following tokens

					String message = "";
					for (int i = 1; i < tokens.length; i++)
						message += tokens[i] + " ";

					var alert = ((ServerErrorAlert) aetherAlerts.get("aetherServerError"));
					alert.setMessage(message);
					alert.show();
				} else if (tokens[0].equalsIgnoreCase("stop"))
					connectedToServerProperty.set(false);
			}

		});

		primaryStage.setOnCloseRequest(event -> {
			// Stop Server Listener Service
			if (aetherSListener != null && aetherSListener.isRunning()) {
				aetherSListener.cancel();
			}

			// Close Client Connection
			if (aetherClient != null) {
				try {
					aetherClient.closeClient();
				} catch (Exception e) {
					Logger.error(e, "Error while closing AetherClient");
				}
			}
		});

		try {
			aetherClient = new AetherClient(
					new Socket(AetherConstants.AETHER_SERVER_IP, AetherConstants.AETHER_SERVER_PORT),
					"FXClient_" + Aether.rnJesus.nextInt(11111, 99999));
			connectedToServerProperty.set(true);
		} catch (Exception e) {
			connectedToServerProperty.set(false);
		}

	}

	public void switchScreen(String screenId) {
		primaryStage.getScene().setRoot(aetherScreens.get(screenId).getRootContainer());
	}

	public void setAetherClient(AetherClient aetherClient) {
		this.aetherClient = aetherClient;
	}

	public AetherClient getAetherClient() {
		return aetherClient;
	}

	public BooleanProperty connectedToServerProperty() {
		return connectedToServerProperty;
	}

	public StringProperty serverMessageProperty() {
		return serverMessageProperty;
	}

	public Stage getPrimaryStage() {
		return primaryStage;
	}

	public HashMap<String, AetherScreen> getAetherScreens() {
		return aetherScreens;
	}

	public HashMap<String, Alert> getAetherAlerts() {
		return aetherAlerts;
	}

}

package io.azraein.aether;

import java.io.File;
import java.net.Socket;
import java.util.HashMap;

import org.tinylog.Logger;

import com.google.gson.Gson;

import io.azraein.aether.account.AetherAccount;
import io.azraein.aether.alerts.AboutAlert;
import io.azraein.aether.alerts.DisconnectedAlert;
import io.azraein.aether.alerts.NoConnectionAlert;
import io.azraein.aether.alerts.ServerErrorAlert;
import io.azraein.aether.screens.AetherHubScreen;
import io.azraein.aether.screens.AetherLoginScreen;
import io.azraein.aether.screens.AetherScreen;
import io.azraein.aether.server.AetherClient;
import io.azraein.aether.utils.*;
import javafx.application.Application;
import javafx.beans.property.*;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.DirectoryChooser;
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
	private final ObjectProperty<AetherAccount> loggedinAccountProperty = new SimpleObjectProperty<>();

	private Gson gson;

	@Override
	public void start(Stage primaryStage) throws Exception {
		this.primaryStage = primaryStage;

		gson = new Gson();

		// Screens
		aetherScreens.put("loginScreen", new AetherLoginScreen(this));
		aetherScreens.put("hubScreen", new AetherHubScreen(this));

		// Alerts
		aetherAlerts.put("aetherDisconnect", new DisconnectedAlert());
		aetherAlerts.put("aetherAbout", new AboutAlert());
		aetherAlerts.put("aetherNoConnection", new NoConnectionAlert());
		aetherAlerts.put("aetherServerError", new ServerErrorAlert("", ""));

		primaryStage.setScene(new Scene(aetherScreens.get("loginScreen").getRootContainer(), 1280, 720));
		primaryStage.setTitle("Aether E-Portal");
		primaryStage.show();

		// Check to see if the Settings file exists. That's our way of telling if it's
		// first start up.

		// TODO: Figure out if any other type of settings are needed
		if (!AetherConfig.doesConfigExist(AetherConfig.AETHER_CLIENT_CONFIG)) {
			Logger.debug("Can't find the AetherClient Config");

			// Create the AetherClient Config file
			File config = new File(AetherConfig.AETHER_CLIENT_CONFIG);
			config.createNewFile();

			DirectoryChooser dc = new DirectoryChooser();
			File directory = dc.showDialog(primaryStage);

			if (directory != null) {
				Logger.debug("Choosing directory: " + directory.getName());
				AetherConfig.writeStringValue("client_settings", "workingDir", directory.getPath(), config);
			} else {
				Logger.debug("Using default I guess");
				AetherConfig.writeStringValue("client_settings", "workingDir", FileUtils.AETHER_WORKING_DIRECTORY,
						config);
			}

			AetherConfig.writeStringValue("server_connection", "ip", AetherConstants.AETHER_SERVER_IP, config);
			AetherConfig.writeIntValue("server_connection", "port", AetherConstants.AETHER_SERVER_PORT, config);

		} else {
			// Found the Config, Time to get the data we need
			File config = new File(AetherConfig.AETHER_CLIENT_CONFIG);
			FileUtils.AETHER_WORKING_DIRECTORY = AetherConfig.getString("client_settings", "workingDir", config);
			AetherConstants.AETHER_SERVER_IP = AetherConfig.getString("server_connection", "ip", config);
			AetherConstants.AETHER_SERVER_PORT = AetherConfig.getInt("server_connection", "port", config);

			Logger.debug(FileUtils.AETHER_WORKING_DIRECTORY);

		}

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
					for (int i = 2; i < tokens.length; i++)
						message += tokens[i] + " ";

					var alert = ((ServerErrorAlert) aetherAlerts.get("aetherServerError"));
					alert.setMessage(tokens[1], message);
					alert.show();
				} else if (tokens[0].equalsIgnoreCase("stop"))
					connectedToServerProperty.set(false);
				else if (tokens[0].equalsIgnoreCase("FILE_USER")) {
					// Logging into an account
					AetherAccount account = gson.fromJson(tokens[1], AetherAccount.class);
					loggedinAccountProperty.set(account);
				}
			}

		});

		loggedinAccountProperty.addListener((obs, oldAct, newAct) -> {

			if (newAct != null) {
				Logger.debug("We've logged into a User!");
				switchScreen("hubScreen");
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

	public Gson getAetherGson() {
		return gson;
	}

	public BooleanProperty connectedToServerProperty() {
		return connectedToServerProperty;
	}

	public StringProperty serverMessageProperty() {
		return serverMessageProperty;
	}

	public ObjectProperty<AetherAccount> loggedinAccountProperty() {
		return loggedinAccountProperty;
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

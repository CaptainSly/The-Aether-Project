package io.azraein.aether;

import org.tinylog.Logger;

import io.azraein.aether.services.AetherServerService;
import io.azraein.aether.utils.AetherConstants;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;
import javafx.util.Callback;

public class AetherServerUI extends Application {

	private AetherServer aetherServer;
	private AetherServerService aetherServerService;
	
	private TextArea serverOutputArea;
	private TextField messageInputField;

	private ObservableList<String> clientIds;

	@Override
	public void start(Stage primaryStage) {
		// Initialize the Aether Server

		// Create UI components
		serverOutputArea = new TextArea();
		serverOutputArea.setEditable(false);
		messageInputField = new TextField();
		Button sendButton = new Button("Send");
		Button startServerButton = new Button("Start Server");
		Button stopServerButton = new Button("Stop Server");

		ListView<String> clientList = new ListView<>();
		clientList.setMaxWidth(100);

		aetherServer = new AetherServer(this);
		clientIds = FXCollections.observableArrayList();

		clientList.setItems(clientIds);

		// Setup the Client ListView

		clientList.setCellFactory(new Callback<ListView<String>, ListCell<String>>() {

			@Override
			public ListCell<String> call(ListView<String> param) {
				return new ListCell<String>() {
					@Override
					protected void updateItem(String item, boolean empty) {
						super.updateItem(item, empty);

						if (item != null) {
							this.setText(item);

							// Setup Context Menu
							ContextMenu clientContext = new ContextMenu();
							MenuItem whisperClient = new MenuItem("WHISPER");
							whisperClient.setOnAction(e -> messageInputField.setText("WHISPER " + item + " "));

							clientContext.getItems().add(whisperClient);
							this.setContextMenu(clientContext);

						} else
							this.setText("");
					}
				};
			}

		});

		// Set actions for the buttons
		sendButton.setOnAction(e -> sendMessage());
		startServerButton.setOnAction(e -> startServer());
		stopServerButton.setOnAction(e -> stopServer());

		// Layout for the UI
		GridPane gridPane = new GridPane();
		gridPane.setHgap(10);
		gridPane.setVgap(10);
		gridPane.add(messageInputField, 0, 0);
		gridPane.add(sendButton, 1, 0);
		gridPane.add(startServerButton, 0, 1);
		gridPane.add(stopServerButton, 1, 1);

		GridPane.setHgrow(messageInputField, Priority.ALWAYS);

		BorderPane root = new BorderPane();
		root.setPadding(new Insets(15));
		root.setCenter(serverOutputArea);
		root.setBottom(gridPane);
		root.setRight(clientList);

		// Create the scene and set it on the stage
		Scene scene = new Scene(root, 600, 340);
		primaryStage.setScene(scene);
		primaryStage.setTitle("Aether Server UI");
		primaryStage.setOnCloseRequest(e -> {

			aetherServer.stopServer();

		});

		// Show the stage
		primaryStage.show();
	}

	private void sendMessage() {
		String message = messageInputField.getText();
		if (!message.isEmpty()) {

			// Process Server Commands before sending them out to (a) client(s)

			// Breaks the message up into tokens for parsing
			String msgTokens[] = message.split(" ");

			// COMMAND - ARGUMENTS
			// TODO: Either write a class is considered a dictionary of commands

			if (msgTokens.length < 2) {
				println("SERVER ERROR: NOT ENOUGH ARGUMENTS");
			}

			// Get The Command

			// Token 0 = Command
			if (msgTokens[0].equalsIgnoreCase("WHISPER")) {
				// Token 1 = Client
				// Token 2 = Message

				// Merge any straglers
				String msg = "";
				for (int i = 2; i < msgTokens.length; i++) {
					msg += msgTokens[i] + " ";
				}

				Logger.debug(msg);

				if (aetherServer.getClients().containsKey(msgTokens[1])) {
					aetherServer.sendMessage(msgTokens[1], msg);
				} else {
					println("SERVER ERROR: NOT ENOUGH ARGUMENTS");
					println("USAGE: WHISPER [client_id] [message]");
				}
			} else if (msgTokens[0].equalsIgnoreCase("BROADCAST")) {
				// Token 1 = Message
				// Merge any straglers
				String msg = "";
				for (int i = 1; i < msgTokens.length; i++) {
					msg += msgTokens[i] + " ";
				}

				aetherServer.broadcastMessage(msg);
			}
			messageInputField.clear();

		}
	}

	public void println(String message) {
		serverOutputArea.appendText(message + "\n");
	}

	private void startServer() {
		println("Starting Aether Server on Port: " + AetherConstants.AETHER_SERVER_PORT);
		aetherServerService = new AetherServerService(aetherServer);
		aetherServerService.start();
	}

	private void stopServer() {
		if (aetherServerService != null) {
			aetherServerService.cancel();
			aetherServerService.setOnCancelled(event -> Platform.exit());
		} else
			Platform.exit();
	}

	public TextArea getServerOutputArea() {
		return serverOutputArea;
	}

	public TextField getMessageInputField() {
		return messageInputField;
	}

	public ObservableList<String> getClientIds() {
		return clientIds;
	}

}

package io.azraein.aether.screens;

import java.io.IOException;
import java.net.Socket;

import org.controlsfx.control.StatusBar;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.materialdesign2.*;

import io.azraein.aether.AetherFX;
import io.azraein.aether.account.AetherAccount;
import io.azraein.aether.server.AetherClient;
import io.azraein.aether.utils.Aether;
import io.azraein.aether.utils.AetherConstants;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;

public abstract class AetherScreen {

	protected BorderPane rootContainer;
	protected MenuBar screenMenubar;
	protected StatusBar screenStatusBar;

	protected AetherFX parent;

	protected final ObjectProperty<AetherAccount> aetherScreenAccountProperty = new SimpleObjectProperty<>();

	public AetherScreen(AetherFX parent) {
		this.parent = parent;
		rootContainer = new BorderPane();

		screenMenubar = new MenuBar();
		screenMenubar.setPadding(new Insets(5, 15, 5, 15));

		screenStatusBar = new StatusBar();

		// Setup Menubar
		setupMenuBar();

		// Setup StatusBar

		rootContainer.setTop(screenMenubar);
		rootContainer.setBottom(screenStatusBar);
	}

	protected void switchScreen(String screenId) {
		parent.switchScreen(screenId);
	}

	protected void setContent(Node content) {
		rootContainer.setCenter(content);
	}

	private void setupMenuBar() {
		Menu settingsMenu = new Menu("Settings");
		settingsMenu.setGraphic(FontIcon.of(MaterialDesignC.COG, 32, Color.BLACK));

		// Account Menu
		MenuItem accountItem = new MenuItem("Account");
		accountItem.setGraphic(FontIcon.of(MaterialDesignA.ACCOUNT_BOX, 32, Color.GREEN));
		accountItem.setDisable(true);
		
		
		// About Menu
		MenuItem aboutItem = new MenuItem("About");
		aboutItem.setGraphic(FontIcon.of(MaterialDesignP.PAC_MAN, 32, Color.YELLOW));
		aboutItem.setOnAction(e -> parent.getAetherAlerts().get("aetherAbout").show());

		// Connection Menu
		Menu connectionMenu = new Menu("Disconnected");
		connectionMenu.setGraphic(FontIcon.of(MaterialDesignL.LAN_DISCONNECT, 32, Color.RED));

		// Network connection
		MenuItem connectItem = new MenuItem("Connect to Server");
		connectItem.setDisable(parent.connectedToServerProperty().get());
		connectItem.setOnAction(e -> {
			try {

				if (parent.getAetherClient() != null)
					parent.getAetherClient().openClient();
				else
					parent.setAetherClient(new AetherClient(
							new Socket(AetherConstants.AETHER_SERVER_IP, AetherConstants.AETHER_SERVER_PORT),
							"FXClient_" + Aether.rnJesus.nextInt(11111, 99999)));

				parent.connectedToServerProperty().set(true);
			} catch (IOException e1) {
				parent.getAetherAlerts().get("aetherDisconnect").showAndWait();
			}
		});

		if (parent.connectedToServerProperty().get()) {
			connectionMenu.setGraphic(FontIcon.of(MaterialDesignL.LAN_CONNECT, 32, Color.GREEN));
			connectionMenu.setText("Connected");
		} else {
			connectionMenu.setGraphic(FontIcon.of(MaterialDesignL.LAN_DISCONNECT, 32, Color.RED));
			connectionMenu.setText("Disconnected");
		}

		parent.connectedToServerProperty().addListener((obs, oldValue, newValue) -> {

			if (newValue) {
				connectionMenu.setGraphic(FontIcon.of(MaterialDesignL.LAN_CONNECT, 32, Color.GREEN));
				connectionMenu.setText("Connected");

				screenStatusBar.setText("Connected to the Aether Server");
			} else {
				connectionMenu.setGraphic(FontIcon.of(MaterialDesignL.LAN_DISCONNECT, 32, Color.RED));
				connectionMenu.setText("Disconnected");

				screenStatusBar.setText("Disconnected from the Aether Server");
			}

			connectItem.setDisable(newValue);

		});
		
		parent.loggedinAccountProperty().addListener((obs, oldAct, newAct) -> {
			
			if (newAct != null) {
				
				accountItem.setDisable(false);
				
			}
			
		});

		settingsMenu.getItems().addAll(accountItem, aboutItem);
		connectionMenu.getItems().addAll(connectItem);

		screenMenubar.getMenus().addAll(settingsMenu, connectionMenu);
	}

	public void setAetherScreenUser(AetherAccount aetherAccount) {
		aetherScreenAccountProperty.set(aetherAccount);
	}

	public BorderPane getRootContainer() {
		return rootContainer;
	}

	public ObjectProperty<AetherAccount> aetherScreenAccountProperty() {
		return aetherScreenAccountProperty;
	}

	public AetherAccount getScreenCurrentUser() {
		return aetherScreenAccountProperty.get();
	}

}

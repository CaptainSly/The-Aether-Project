package io.azraein.aether.screens;

import org.tinylog.Logger;

import io.azraein.aether.AetherFX;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class AetherLoginScreen extends AetherScreen {

	private HBox aetherRootBox;
	private VBox aetherCompanyBox;
	private VBox aetherLoginBox;

	private Label aetherCompanyLbl, aetherSloganLbl;

	public AetherLoginScreen(AetherFX parent) {
		super(parent);

		aetherRootBox = new HBox();
		aetherCompanyBox = new VBox();
		aetherLoginBox = new VBox();

		aetherCompanyLbl = new Label("Anzel Electronics");
		aetherSloganLbl = new Label("Unleashing Tomorrow's Horizons Today!");

		GridPane loginGrid = new GridPane();
		loginGrid.setHgap(10);
		loginGrid.setVgap(10);

		Label userloginLbl = new Label("Username:");
		Label userpassLbl = new Label("Password:");

		TextField userLoginFld = new TextField();
		userLoginFld.setPromptText("Username...");

		PasswordField userPassFld = new PasswordField();
		userPassFld.setPromptText("Password...");

		Button loginBtn = new Button("Login");
		Button registerBtn = new Button("Register");

		loginBtn.setOnAction(e -> {

			if (!parent.connectedToServerProperty().get()) {
				parent.getAetherAlerts().get("aetherNoConnection").show();
				return;
			}

			String username = userLoginFld.getText();
			String password = userPassFld.getText();

			Logger.debug("Trying to login with credentials: " + username + " " + password);
			parent.getAetherClient().getClientWriter().println("login " + username + " " + password);

		});

		registerBtn.setOnAction(e -> {

			if (!parent.connectedToServerProperty().get()) {
				parent.getAetherAlerts().get("aetherNoConnection").show();
				return;
			}

			String username = userLoginFld.getText();
			String password = userPassFld.getText();

			Logger.debug("Trying to create account with " + username + " " + password);
			parent.getAetherClient().getClientWriter().println("user " + username + " " + password);

		});

		loginGrid.add(userloginLbl, 0, 0);
		loginGrid.add(userLoginFld, 1, 0);
		loginGrid.add(userpassLbl, 0, 1);
		loginGrid.add(userPassFld, 1, 1);
		loginGrid.add(loginBtn, 0, 2);
		loginGrid.add(registerBtn, 1, 2);

		GridPane.setHalignment(loginBtn, HPos.CENTER);
		GridPane.setHalignment(registerBtn, HPos.CENTER);

		aetherRootBox.setAlignment(Pos.CENTER);
		aetherRootBox.setPadding(new Insets(15));
		aetherRootBox.getChildren().addAll(aetherCompanyBox, aetherLoginBox);

		aetherCompanyBox.setAlignment(Pos.CENTER);
		aetherCompanyBox.setPadding(new Insets(35));
		aetherCompanyBox.getChildren().addAll(aetherCompanyLbl, aetherSloganLbl);

		aetherLoginBox.setAlignment(Pos.CENTER);
		aetherLoginBox.setPadding(new Insets(35));
		aetherLoginBox.getChildren().add(loginGrid);

		this.setContent(aetherRootBox);
	}

}

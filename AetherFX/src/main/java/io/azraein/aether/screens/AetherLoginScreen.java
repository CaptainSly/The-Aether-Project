package io.azraein.aether.screens;

import io.azraein.aether.AetherFX;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

public class AetherLoginScreen extends AetherScreen {

	private VBox aetherCompanyBox;
	private VBox aetherLoginBox;

	private Label aetherCompanyLbl, aetherSloganLbl;
	
	public AetherLoginScreen(AetherFX parent) {
		super(parent);

		aetherCompanyBox = new VBox();
		aetherLoginBox = new VBox();
		
		aetherCompanyLbl = new Label("Anzel Electronics");
		aetherSloganLbl = new Label("Unleashing Tomorrow's Horizons Today!");
		
		GridPane loginGrid = new GridPane();
		
		aetherCompanyBox.setAlignment(Pos.CENTER);
		aetherCompanyBox.setPadding(new Insets(35));
		aetherCompanyBox.getChildren().addAll(aetherCompanyLbl, aetherSloganLbl);
		
		aetherLoginBox.setAlignment(Pos.CENTER);
		aetherLoginBox.setPadding(new Insets(35));
		aetherLoginBox.getChildren().add(loginGrid);
				
		BorderPane.setAlignment(aetherCompanyBox, Pos.CENTER_LEFT);
		BorderPane.setAlignment(aetherLoginBox, Pos.CENTER_RIGHT);
		
		this.getRootContainer().setLeft(aetherCompanyBox);
		this.getRootContainer().setRight(aetherLoginBox);
	}

}

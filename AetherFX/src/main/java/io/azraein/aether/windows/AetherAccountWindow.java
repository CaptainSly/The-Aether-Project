package io.azraein.aether.windows;

import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.materialdesign2.MaterialDesignA;
import org.tinylog.Logger;

import io.azraein.aether.AetherFX;
import javafx.scene.control.Button;

public class AetherAccountWindow extends AetherWindow {

	public AetherAccountWindow(AetherFX parent) {
		super(parent, "aetherWindow_accounts", FontIcon.of(MaterialDesignA.ACCOUNT, 24), "Aether Accounts");
		Button testBtn = new Button("Test");
		testBtn.setOnAction(e -> {
			Logger.debug(parent.loggedinAccountProperty().get().getAetherAccountUser().getAetherUserName());
		});

		root.setCenter(testBtn);
	}

}

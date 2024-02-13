package io.azraein.aether.screens;

import org.kordamp.desktoppanefx.scene.layout.DesktopPane;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.materialdesign2.MaterialDesignA;
import org.kordamp.ikonli.materialdesign2.MaterialDesignE;
import org.kordamp.ikonli.materialdesign2.MaterialDesignM;
import org.kordamp.ikonli.materialdesign2.MaterialDesignN;

import io.azraein.aether.AetherFX;
import io.azraein.aether.windows.AetherAccountWindow;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class AetherHubScreen extends AetherScreen {

	private VBox hubDrawer;
	private DesktopPane hubDesktop;

	public AetherHubScreen(AetherFX parent) {
		super(parent);

		hubDrawer = new VBox();
		hubDrawer.setPadding(new Insets(25));
		hubDrawer.setSpacing(100);
		hubDrawer.setAlignment(Pos.CENTER);

		// Setup Hub Buttons
		Button accountBtn = new Button();
		accountBtn.setGraphic(FontIcon.of(MaterialDesignA.ACCOUNT_BOX_OUTLINE, 64, Color.BLUE));
		accountBtn.setOnAction(e -> {
			AetherAccountWindow aaw = new AetherAccountWindow(parent);
			hubDesktop.addInternalWindow(aaw);
		});

		Button msgsBtn = new Button();
		msgsBtn.setGraphic(FontIcon.of(MaterialDesignM.MESSAGE_ALERT, 64, Color.FORESTGREEN));

		Button aNewsBtn = new Button();
		aNewsBtn.setGraphic(FontIcon.of(MaterialDesignN.NEWSPAPER_PLUS, 64, Color.DARKGRAY));

		Button emailBtn = new Button();
		emailBtn.setGraphic(FontIcon.of(MaterialDesignE.EMAIL, 64, Color.RED));

		hubDrawer.getChildren().addAll(accountBtn, msgsBtn, aNewsBtn, emailBtn);

		hubDesktop = new DesktopPane();

		this.getRootContainer().setLeft(hubDrawer);
		this.getRootContainer().setCenter(hubDesktop);

	}

}

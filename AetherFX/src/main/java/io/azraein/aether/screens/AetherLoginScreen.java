package io.azraein.aether.screens;

import io.azraein.aether.AetherFX;
import io.azraein.aether.alerts.AboutAlert;
import io.azraein.aether.utils.Aether;
import javafx.scene.control.Button;

public class AetherLoginScreen extends AetherScreen {

	public String[] names = { "Dave", "Phil", "Carlos", "LOUIS!!!!", "James" };

	public AetherLoginScreen(AetherFX parent) {
		super(parent);

		Button test = new Button("TEST MKACT");
		test.setOnAction(e -> {

			var client = parent.getAetherClient();

			client.getClientWriter().println("MKACT " + names[Aether.rnJesus.nextInt(names.length)] + " password");

		});

		Button test2 = new Button("ListUser");
		test2.setOnAction(e -> {
			var client = parent.getAetherClient();

			client.getClientWriter().println("LISTUSERS");
		});

		this.getRootContainer().setCenter(test);
		this.getRootContainer().setBottom(test2);
	}

}

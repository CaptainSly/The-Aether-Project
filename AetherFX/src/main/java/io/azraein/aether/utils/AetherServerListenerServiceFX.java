package io.azraein.aether.utils;

import java.io.IOException;

import org.tinylog.Logger;

import io.azraein.aether.AetherFX;
import io.azraein.aether.server.AetherClient;
import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

public class AetherServerListenerServiceFX extends Service<Void> {

	private AetherFX aetherFX;
	private AetherClient client;

	public AetherServerListenerServiceFX(AetherFX aetherFX, AetherClient client) {
		this.aetherFX = aetherFX;
		this.client = client;
	}

	@Override
	protected Task<Void> createTask() {
		return new AetherServiceTask();
	}

	@Override
	public boolean cancel() {
		client.closeClient();
		return super.cancel();
	}

	private class AetherServiceTask extends Task<Void> {

		@Override
		protected Void call() throws Exception {
			try {

				String line;
				while ((line = client.getClientReader().readLine()) != null) {
					final String sub = line;
					Logger.debug("Received Message: '" + line + "' from server");
					Platform.runLater(() -> aetherFX.serverMessageProperty().set(sub));
				}

			} catch (IOException e) {
				Logger.error(e, "Error in AetherServerListenerServiceFX task");
				client.closeClient();
			}
			return null;
		}

	}

}

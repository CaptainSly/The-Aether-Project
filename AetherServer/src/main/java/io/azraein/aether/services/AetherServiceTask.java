package io.azraein.aether.services;

import io.azraein.aether.AetherServer;
import javafx.concurrent.Task;

public class AetherServiceTask extends Task<Void> {

	private AetherServer aetherServer;

	public AetherServiceTask(AetherServer aetherServer) {
		this.aetherServer = aetherServer;
	}

	@Override
	protected Void call() throws Exception {
		aetherServer.startServer();
		return null;
	}

}

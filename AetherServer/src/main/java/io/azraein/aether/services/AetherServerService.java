package io.azraein.aether.services;

import io.azraein.aether.AetherServer;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

public class AetherServerService extends Service<Void> {

	private AetherServer aetherServer;

	public AetherServerService(AetherServer aetherServer) {
		this.aetherServer = aetherServer;
	}

	@Override
	protected Task<Void> createTask() {
		return new AetherServiceTask(aetherServer);
	}

	@Override
	public boolean cancel() {
		aetherServer.stopServer();		
		return super.cancel();
	}

}

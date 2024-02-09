package io.azraein.aether;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.tinylog.Logger;

import io.azraein.aether.account.AetherUser;
import io.azraein.aether.server.AetherClient;
import io.azraein.aether.utils.Aether;
import io.azraein.aether.utils.AetherConstants;
import io.azraein.aether.utils.AetherSecurity;
import javafx.application.Platform;

public class AetherServer {

	private ServerSocket aetherServerSocket;
	private ExecutorService clientThreadPool;
	private Map<String, AetherClient> clients;

	// AetherUsers
	private Map<String, AetherUser> aetherUsers;

	private final AetherServerUI aetherServerUI;

	public AetherServer(AetherServerUI aetherServerUI) {
		this.aetherServerUI = aetherServerUI;

		clientThreadPool = Executors.newCachedThreadPool();
		clients = new ConcurrentHashMap<>();
		aetherUsers = new ConcurrentHashMap<>();
	}

	public void startServer() throws IOException {
		aetherServerSocket = new ServerSocket(AetherConstants.AETHER_SERVER_PORT);
		listenForClients();
	}

	private void listenForClients() {

		if (!aetherServerSocket.isClosed()) {
			try {
				while (true) {
					Socket clientSocket = aetherServerSocket.accept();
					handleClient(clientSocket);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private void handleClient(Socket clientSocket) {
		try {
			AetherClient client = new AetherClient(clientSocket, "client_" + Aether.rnJesus.nextInt(11111, 99999));
			clients.put(client.getClientId(), client);
			Platform.runLater(() -> {
				// Add the client to the observable list
				aetherServerUI.getClientIds().add(client.getClientId());
			});

			clientThreadPool.execute(() -> {
				try {
					String line;
					while ((line = client.getClientReader().readLine()) != null) {
						// Process the received message from the client
						processClientMessage(client.getClientId(), line);
					}
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					// Remove the client writer when the client disconnects
					Platform.runLater(
							() -> aetherServerUI.println("Client " + client.getClientId() + " has disconnected"));

					client.closeClient();
					aetherServerUI.getClientIds().remove(aetherServerUI.getClientIds().indexOf(client.getClientId()));
					clients.remove(client.getClientId());
					try {
						clientSocket.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			});

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void processClientMessage(String clientId, String message) {
		Logger.debug("Recieved message from client: " + clientId);

		Platform.runLater(() -> {
			aetherServerUI.println("Client " + clientId + ": " + message);
		});

		// TODO: Process Client Messages
		String[] msgTokens = message.split(" ");

		// COMMAND - ARGS
		if (msgTokens[0].equalsIgnoreCase("LOGIN")) { // Login to Account
			// Token 1 = USERNAME
			// Token 2 = PLAINTEXT PASSWORD

			AetherUser user = null;
			for (AetherUser u : aetherUsers.values()) {
				if (u.getAetherUserName().equals(msgTokens[1])) {
					user = u;
					break;
				}
			}

			if (user != null) {
				// Check to make sure password is correct
				String userDecryptedPass = AetherSecurity.decrypt(user.getAetherEncryptedPassword(),
						user.getAetherPassPhrase());

				if (userDecryptedPass.equals(msgTokens[1])) {
					// TODO: Send over the correct data that needs to be sent. For now just send the
					// client a message that it was successful.
					final AetherUser d = user;
					Platform.runLater(
							() -> aetherServerUI.println("Successful login to account: " + d.getAetherUserName()));
					clients.get(clientId).getClientWriter().println("LOGIN TRUE");

				}
			}

		} else if (msgTokens[0].equalsIgnoreCase("MKACT")) { // Create Account
			// Token 1 = USERNAME
			// Token 2 = PLAINTEXT PASSWORD
			Logger.debug("Creating Account");

			// Create AetherUser
			AetherUser newUser = new AetherUser("employee_" + Aether.rnJesus.nextInt(1000, 9999), msgTokens[1]);
			newUser.setPassword(msgTokens[2]);

			// Put the user into the account
			aetherUsers.put(newUser.getAetherUserId(), newUser);
		} else if (msgTokens[0].equalsIgnoreCase("LISTUSERS")) {
			Platform.runLater(() -> {

				for (AetherUser u : aetherUsers.values())
					aetherServerUI.println(u.getAetherUserName());

			});
		}

	}

	public void sendMessage(String clientId, String message) {
		clients.get(clientId).getClientWriter().println(message);
	}

	public void broadcastMessage(String message) {
		for (AetherClient client : clients.values())
			client.getClientWriter().println(message);
	}

	public void stopServer() {
		broadcastMessage("STOP");

		clientThreadPool.shutdownNow();
		try {
			if (aetherServerSocket != null && !aetherServerSocket.isClosed()) {
				aetherServerSocket.close();
				System.out.println("Aether Server stopped");
			}
		} catch (IOException e) {
			Logger.warn("The socket was already null, or something, it should be all good");
		}
	}

	public ServerSocket getSocket() {
		return aetherServerSocket;
	}

	public ServerSocket getAetherServerSocket() {
		return aetherServerSocket;
	}

	public ExecutorService getClientThreadPool() {
		return clientThreadPool;
	}

	public Map<String, AetherClient> getClients() {
		return clients;
	}

	public Map<String, AetherUser> getAccounts() {
		return aetherUsers;
	}

	public void setClientThreadPool(ExecutorService clientThreadPool) {
		this.clientThreadPool = clientThreadPool;
	}

}

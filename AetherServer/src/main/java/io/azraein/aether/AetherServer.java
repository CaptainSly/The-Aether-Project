package io.azraein.aether;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.tinylog.Logger;

import com.google.gson.Gson;

import io.azraein.aether.account.AetherAccount;
import io.azraein.aether.account.AetherAccount.AccountRole;
import io.azraein.aether.account.AetherUser;
import io.azraein.aether.server.AetherClient;
import io.azraein.aether.server.commands.AetherCommand;
import io.azraein.aether.utils.Aether;
import io.azraein.aether.utils.AetherConstants;
import io.azraein.aether.utils.AetherSecurity;
import javafx.application.Platform;

public class AetherServer {

	private ServerSocket aetherServerSocket;
	private ExecutorService clientThreadPool;
	private Map<String, AetherClient> clients;

	private Map<String, AetherCommand> commands;

	// AetherAccounts
	private Map<String, AetherAccount> aetherAccounts;

	private final AetherServerUI aetherServerUI;

	private Gson gson;

	public AetherServer(AetherServerUI aetherServerUI) {
		this.aetherServerUI = aetherServerUI;

		gson = new Gson();

		clientThreadPool = Executors.newCachedThreadPool();
		clients = new ConcurrentHashMap<>();
		aetherAccounts = new ConcurrentHashMap<>();

		aetherAccounts.put("testUser", new AetherAccount(new AetherUser("testUser", "password"), AccountRole.USER));

		commands = new HashMap<>();

		initCommands();
	}

	public void startServer() throws IOException {
		aetherServerSocket = new ServerSocket(AetherConstants.AETHER_SERVER_PORT);
		listenForClients();
	}

	private void initCommands() {
		commands.put("user", new AetherCommand("user", "Creates a new user by supplying the username and password", 2));

		commands.put("login", new AetherCommand("login", "Logs the provided user into the server if they exist", 2));

		commands.put("help", new AetherCommand("help", "Provides help information about a command", 1));
		commands.put("whisper",
				new AetherCommand("whisper", "Sends a message to a specific client based off the clientId", 2));

		commands.put("broadcast",
				new AetherCommand("broadcast", "Broadcasts a message to every client connected to the server", 1));
		commands.put("error", new AetherCommand("error", "Throw an Error on the Client, has no effect on Server", 2));
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

		String[] msgTokens = message.split(" ");
		processCommand(msgTokens, clientId);
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

	public void processCommand(String[] tokens, String clientId) {
		var client = clients.get(clientId);
		// Check to see if the first token is inside the commands map
		String command = tokens[0];
		if (tokens.length < 2) {
			client.getClientWriter().println("error COMMAND_ERROR Malformed Command sent to server");
			return;
		}

		if (commands.containsKey(command)) {

			// Get the Command Record
			AetherCommand cmd = commands.get(command);
			if (cmd.commandName().equalsIgnoreCase("whisper")) {
				sendMessage(tokens[1], combineTokens(tokens, 2));
			} else if (cmd.commandName().equalsIgnoreCase("broadcast")) {
				broadcastMessage(combineTokens(tokens, 1));
			} else if (cmd.commandName().equalsIgnoreCase("login")) {

				// Login time
				String username = tokens[1];
				String password = tokens[2];

				if (aetherAccounts.containsKey(username)) {

					var user = aetherAccounts.get(username).getAetherAccountUser();

					// Check the passwords against each other
					if (password.equals(
							AetherSecurity.decrypt(user.getAetherEncryptedPassword(), user.getAetherPassPhrase()))) {

						Logger.debug("Correct password");
						client.getClientWriter().println("FILE_USER " + gson.toJson(aetherAccounts.get(username)));

					}

				} else {
					client.getClientWriter().println("error ACCOUNT_ERROR User " + username + " does not exist!");

				}

			} else if (cmd.commandName().equalsIgnoreCase("user")) {

				// Register Time
				String username = tokens[1];
				String password = tokens[2];
				int accountType = Integer.parseInt(tokens[3]);

				if (aetherAccounts.containsKey(username)) {
					// TODO: throw an error, towards the client that the user already exists.
					client.getClientWriter().println("error ACCOUNT_ERROR User " + username + " already exists!");
					return;
				}

				// TODO: Create User Account

			}

		}

	}

	public String combineTokens(String[] tokens, int idx) {
		String str = "";
		for (int i = idx; i < tokens.length; i++)
			str += tokens[i] + " ";

		return str;
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

	public Map<String, AetherAccount> getAccounts() {
		return aetherAccounts;
	}

	public Map<String, AetherCommand> commands() {
		return commands;
	}

	public void setClientThreadPool(ExecutorService clientThreadPool) {
		this.clientThreadPool = clientThreadPool;
	}

}

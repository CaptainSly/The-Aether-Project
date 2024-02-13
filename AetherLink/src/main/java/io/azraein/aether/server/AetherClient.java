package io.azraein.aether.server;

import java.io.*;
import java.net.Socket;

import io.azraein.aether.utils.AetherConstants;

public class AetherClient {

	private final String clientId;

	private Socket clientSocket;

	// Sending and Receiving Strings
	private PrintWriter clientStrWriter;
	private BufferedReader clientStrReader;

	public AetherClient(Socket clientSocket, String clientId) throws IOException {
		this.clientSocket = clientSocket;
		this.clientId = clientId;

		this.clientStrWriter = new PrintWriter(clientSocket.getOutputStream(), true);
		this.clientStrReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
		
		getClientWriter().println("broadcast " + clientId + " connected to Aether Server");
	}

	public void openClient() throws IOException {
		this.clientSocket = new Socket(AetherConstants.AETHER_SERVER_IP, AetherConstants.AETHER_SERVER_PORT);

		this.clientStrWriter = new PrintWriter(clientSocket.getOutputStream(), true);
		this.clientStrReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

		getClientWriter().println("broadcast " + clientId + " connected to Aether Server");
	}

	public void closeClient() {
		try {
			clientStrWriter.close();
			clientStrReader.close();

			clientSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Socket getClientSocket() {
		return clientSocket;
	}

	public String getClientId() {
		return clientId;
	}

	public PrintWriter getClientWriter() {
		return clientStrWriter;
	}

	public BufferedReader getClientReader() {
		return clientStrReader;
	}

}

package io.azraein.aether.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import io.azraein.aether.utils.AetherConstants;

public class AetherClient {

	private final String clientId;

	private Socket clientSocket;
	private PrintWriter clientWriter;
	private BufferedReader clientReader;

	public AetherClient(Socket clientSocket, String clientId) throws IOException {
		this.clientSocket = clientSocket;
		this.clientId = clientId;
		this.clientWriter = new PrintWriter(clientSocket.getOutputStream(), true);
		this.clientReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

		clientWriter.println("Client " + clientId + " has connected");
	}

	public void openClient() throws IOException {
		this.clientSocket = new Socket(AetherConstants.AETHER_SERVER_IP, AetherConstants.AETHER_SERVER_PORT);
		this.clientWriter = new PrintWriter(clientSocket.getOutputStream(), true);
		this.clientReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
	}

	public void closeClient() {
		try {
			clientWriter.close();
			clientReader.close();
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
		return clientWriter;
	}

	public BufferedReader getClientReader() {
		return clientReader;
	}

}

package io.azraein.aether;

import io.azraein.aether.account.AetherUser;
import javafx.application.Application;

public class Main {

	public static void main(String[] args) {
		
		AetherUser a = new AetherUser("Test", "Test");
		a.setPassword("QuackenDacken");
		
		Application.launch(AetherServerUI.class, args);
	}

}

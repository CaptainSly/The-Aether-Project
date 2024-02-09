package io.azraein.aether.utils;

public class AetherSecurity {

	public static final String[] CODE_PHRASES = { "Azathoth", "YogSothoth", "ShubNiggurath", "Dagon", "Hastur", "Yig",
			"Ithaqua", "Cthugha", "Tsathoggua", "Nodens" };

	public static String encrypt(String text, String codePhrase) {
		StringBuilder encryptedText = new StringBuilder();

		for (int i = 0; i < text.length(); i++) {
			char textChar = text.charAt(i);
			char codeChar = codePhrase.charAt(i % codePhrase.length());

			// Add corresponding ASCII values
			char encryptedChar = (char) (textChar + codeChar);

			encryptedText.append(encryptedChar);
		}

		return encryptedText.toString();
	}

	public static String decrypt(String encryptedText, String codePhrase) {
		StringBuilder decryptedText = new StringBuilder();

		for (int i = 0; i < encryptedText.length(); i++) {
			char encryptedChar = encryptedText.charAt(i);
			char codeChar = codePhrase.charAt(i % codePhrase.length());

			// Subtract corresponding ASCII values
			char decryptedChar = (char) (encryptedChar - codeChar);

			decryptedText.append(decryptedChar);
		}

		return decryptedText.toString();
	}

}

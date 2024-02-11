package io.azraein.aether.account;

import io.azraein.aether.utils.Aether;
import io.azraein.aether.utils.AetherSecurity;

public class AetherUser {

	private String aetherUserName;
	private String aetherEncryptedPassword;
	private String aetherPassPhrase;

	// TODO: Password stuff

	public AetherUser(String aetherUserName, String password) {
		this.aetherUserName = aetherUserName;
		setPassword(password);
	}

	private void setPassword(String password) {
		aetherPassPhrase = AetherSecurity.CODE_PHRASES[Aether.rnJesus.nextInt(AetherSecurity.CODE_PHRASES.length)];
		this.aetherEncryptedPassword = AetherSecurity.encrypt(password, aetherPassPhrase);
	}

	public String getAetherUserName() {
		return aetherUserName;
	}

	public String getAetherEncryptedPassword() {
		return aetherEncryptedPassword;
	}

	public String getAetherPassPhrase() {
		return aetherPassPhrase;
	}

}

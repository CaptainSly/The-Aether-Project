package io.azraein.aether.account;

import java.io.Serializable;

import io.azraein.aether.utils.Aether;
import io.azraein.aether.utils.AetherSecurity;

public class AetherUser implements Serializable {

	private static final long serialVersionUID = -7491737714238380215L;

	private String aetherUserName;
	private String aetherEncryptedPassword;
	private String aetherPassPhrase;

	// TODO: Password stuff

	public AetherUser(String aetherUserName, String password) {
		this.aetherUserName = aetherUserName;
		setPassword(password);
	}

	public AetherUser(String aetherUserName, String encryptedPass, String passPhrase) {
		this.aetherUserName = aetherUserName;
		this.aetherEncryptedPassword = encryptedPass;
		this.aetherPassPhrase = passPhrase;
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

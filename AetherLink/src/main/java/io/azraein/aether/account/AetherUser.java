package io.azraein.aether.account;

import io.azraein.aether.utils.Aether;
import io.azraein.aether.utils.AetherSecurity;

public class AetherUser {

	private final String aetherUserId;
	private String aetherUserName;
	private String aetherEncryptedPassword;
	private String aetherPassPhrase;

	// TODO: Password stuff

	public AetherUser(String aetherUserId, String aetherUserName) {
		// TODO: userId scheme is employee0000 where 0000 is their creation order.
		this.aetherUserId = aetherUserId;
		this.aetherUserName = aetherUserName;
	}

	public void setPassword(String password) {
		aetherPassPhrase = AetherSecurity.CODE_PHRASES[Aether.rnJesus.nextInt(AetherSecurity.CODE_PHRASES.length)];
		this.aetherEncryptedPassword = AetherSecurity.encrypt(password, aetherPassPhrase);
	}

	public String getAetherUserId() {
		return aetherUserId;
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

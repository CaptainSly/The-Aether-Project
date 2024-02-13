package io.azraein.aether.account;

import java.io.Serializable;

public class AetherAccount implements Serializable {

	private static final long serialVersionUID = -8071022864987375363L;

	public enum AccountRole {

		USER, SUPER_USER, ADMIN, MODERATOR

	}

	private final AetherUser aetherAccountUser;
	private String aetherAccountEmail;
	private AccountRole aetherAccountRole;

	public AetherAccount(AetherUser aetherAccountUser, AccountRole accountRole) {
		this.aetherAccountUser = aetherAccountUser;
		this.aetherAccountRole = accountRole;

		// Setup email:
		aetherAccountEmail = aetherAccountUser.getAetherUserName() + "@anzel.org";
	}

	public AetherAccount(AetherUser aetherAccountUser, AccountRole accountRole, String aetherAccountEmail) {
		this.aetherAccountUser = aetherAccountUser;
		this.aetherAccountRole = accountRole;
		this.aetherAccountEmail = aetherAccountEmail;
	}

	public AetherUser getAetherAccountUser() {
		return aetherAccountUser;
	}

	public String getAetherAccountEmail() {
		return aetherAccountEmail;
	}

	public AccountRole getAetherAccountRole() {
		return aetherAccountRole;
	}

}

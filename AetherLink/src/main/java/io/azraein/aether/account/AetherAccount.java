package io.azraein.aether.account;

public class AetherAccount {

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
		String id = aetherAccountUser.getAetherUserId();
		String lastFour = id.substring(id.length() - 4, id.length());
		aetherAccountEmail = aetherAccountUser.getAetherUserName() + lastFour + "@anzel.org";
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
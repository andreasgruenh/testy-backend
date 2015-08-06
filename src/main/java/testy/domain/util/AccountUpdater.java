package testy.domain.util;

import testy.domain.Account;

public class AccountUpdater {

	public static AccountUpdater start(Account account) {
		if(account == null) {
			throw new NullPointerException();
		}
		return new AccountUpdater(account);
	}
	
	private Account account;
	
	private AccountUpdater(Account account) {
		this.account = account;
	}
	
	public AccountUpdater withFirstname(String firstname) {
		account.setFirstname(firstname);
		return this;
	}
	
	public AccountUpdater withLastname(String lastname) {
		account.setLastname(lastname);
		return this;
	}
	
	public AccountUpdater withEmail(String email) {
		account.setEmail(email);
		return this;
	}
	
	public AccountUpdater isAdmin(boolean bool) {
		account.setAdmin(bool);
		return this;
	}
	
	public Account build() {
		return account;
	}
	
}

package testy.domain.util;

import testy.domain.Account;

public class AccountBuilder {

	/*
	 *  You can build new Accounts with this builder or insert an existing account and update its fields conveniently
	 */
	public static AccountBuilder startWithExisting(Account account) {
		if(account == null) {
			throw new NullPointerException();
		}
		return new AccountBuilder(account);
	}
	
	public static AccountBuilder startWith(String accountName) {
		if(accountName == null) {
			throw new NullPointerException();
		}
		return new AccountBuilder(new Account(accountName));
	}
	
	private Account account;
	
	private AccountBuilder(Account account) {
		this.account = account;
	}
	
	public AccountBuilder withFirstname(String firstname) {
		if(firstname == null) {
			return this;
		}
		account.setFirstname(firstname);
		return this;
	}
	
	public AccountBuilder withLastname(String lastname) {
		if(lastname == null) {
			return this;
		}
		account.setLastname(lastname);
		return this;
	}
	
	public AccountBuilder withEmail(String email) {
		if(email == null) {
			return this;
		}
		account.setEmail(email);
		return this;
	}
	
	public AccountBuilder isAdmin(boolean bool) {
		account.setAdmin(bool);
		return this;
	}
	
	public Account build() {
		return account;
	}
	
}

package testy.service;

import static org.junit.Assert.*;

import org.junit.Test;

import testy.domain.Account;
import testy.domain.util.AccountUpdater;

public class AccountUpdaterTest {

	static final String accountname = "hwerner",
			firstname = "Hans",
			lastname = "Werner",
			email = "hans@werner.de";
	static final boolean isAdmin = true;
	
	@Test
	public void builder_shouldReturnNonAdminAccountIfNotSpecified() {
		Account account = AccountUpdater.start(new Account(accountname)).build();
		assertTrue(account.getAccountName().equals(accountname));
		assertFalse(account.isAdmin());
	}
	
	@Test
	public void builder_shouldReturnAccountWithCorrectFields() {
		Account account = AccountUpdater.start(new Account(accountname))
				.withFirstname(firstname)
				.withLastname(lastname)
				.withEmail(email)
				.isAdmin(true)
				.build();
		assertTrue(account.getAccountName().equals(accountname));
		assertTrue(account.getFirstname().equals(firstname));
		assertTrue(account.getLastname().equals(lastname));
		assertTrue(account.getEmail().equals(email));
		assertTrue(account.isAdmin());
	}

}

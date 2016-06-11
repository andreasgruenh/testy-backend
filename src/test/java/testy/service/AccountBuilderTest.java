package testy.service;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import testy.domain.Account;
import testy.domain.util.AccountBuilder;

public class AccountBuilderTest {

	static final String accountname = "hwerner",
			firstname = "Hans",
			lastname = "Werner",
			email = "hans@werner.de";
	static final boolean isAdmin = true;
	
	@Test
	public void builder_shouldReturnNonAdminAccountIfNotSpecified() {
		Account account = AccountBuilder.startWith(accountname).build();
		assertTrue("Accountname should equal expected accountname", account.getAccountName().equals(accountname));
		assertFalse("Admin role should be false if nothing is specified", account.isAdmin());
	}
	
	@Test
	public void builder_shouldReturnAccountWithCorrectFields() {
		Account account = AccountBuilder.startWith(accountname)
				.withFirstname(firstname)
				.withLastname(lastname)
				.withEmail(email)
				.isAdmin(true)
				.build();
		assertTrue("Accountname should equal given accountname", account.getAccountName().equals(accountname));
		assertTrue("Firstname should equal given firstname", account.getFirstname().equals(firstname));
		assertTrue("Lastname should equal given lastname", account.getLastname().equals(lastname));
		assertTrue("Email should equal given email", account.getEmail().equals(email));
		assertTrue("Admin value should equal expected value", account.isAdmin());
	}
	
	@Test
	public void builder_shouldReturnUpdatedAccount() {
		Account account = new Account("tmüller");
		Account newAccount = AccountBuilder.startWithExisting(account)
				.withFirstname(firstname)
				.withLastname(lastname)
				.withEmail(email)
				.isAdmin(true)
				.build();
		assertTrue("The builder should return a reference to the same account object, not to a copy", account == newAccount);
		assertTrue("Accountname should equal expected name", newAccount.getAccountName().equals("tmüller"));
		assertTrue("Firstname should equal given firstname", newAccount.getFirstname().equals(firstname));
		assertTrue("Lastname should equal given lastname", newAccount.getLastname().equals(lastname));
		assertTrue("Email should equal given email", newAccount.getEmail().equals(email));
		assertTrue("Admin value should equal expected value", newAccount.isAdmin());
	}

}

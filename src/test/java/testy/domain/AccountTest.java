package testy.domain;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import testy.domain.util.AccountBuilder;

public class AccountTest {

	Account account = new Account("name");
	
	@Test
	public void setAdminFalse_shouldDoNothingIfAccountIsAroth() {
		Account acc = AccountBuilder.startWith("aroth")
				.isAdmin(true).build();
		acc.setAdmin(false);
		assertTrue("aroth should always be admin", acc.isAdmin());
	}
}

package testy.domain;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import testy.domain.test.TestResult;
import testy.domain.util.AccountBuilder;
import testy.helper.UnmodifiableChecker;

public class AccountTest {

	TestResult result1 = new TestResult();
	Account account = new Account("name");
	
	@Test
	public void setAdminFalse_shouldDoNothingIfAccountIsAroth() {
		Account acc = AccountBuilder.startWith("aroth")
				.isAdmin(true).build();
		acc.setAdmin(false);
		assertTrue("aroth should always be admin", acc.isAdmin());
	}
	
	@Test
	public void addTestResult_shouldAddTestResultAndSetUserOfTestResult() {
		account.addTestResult(result1);
		
		assertTrue("TestResults should contain result", account.getTestResults().contains(result1));
		assertTrue("User of TestResult should be set", result1.getUser() == account);
	}
	
	@Test
	public void getTestResults_shouldReturnUnmodifiableSet() {
		assertTrue("Set should be unmodifiable", UnmodifiableChecker.isUnmodifiable(account.getTestResults()));
	}
	
}

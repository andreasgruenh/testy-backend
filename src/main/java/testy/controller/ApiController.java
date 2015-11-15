package testy.controller;

import testy.domain.Account;

public class ApiController {

	protected Account loggedInAccount;
	
	public void setLoggedInAccount(Account account) {
		loggedInAccount = account;
	}
	
}

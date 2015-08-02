package testy.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import testy.domain.Account;
import testy.security.service.CurrentAccountService;

@RestController
@RequestMapping("/accounts")
public class AccountController {

	@Autowired
	CurrentAccountService accountService;
	
	@RequestMapping(value = "/me", method = RequestMethod.GET)
	Account getCurrentUser() {
		return accountService.getLoggedInAccount();
	}
	
	@RequestMapping(value = "/loggedIn", method = RequestMethod.GET)
	void isLoggedIn() {
		
	}
	
}

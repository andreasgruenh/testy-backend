package testy.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import testy.controller.exception.NotEnoughPermissionsException;
import testy.dataaccess.AccountRepository;
import testy.domain.Account;
import testy.domain.util.AccountBuilder;
import testy.security.service.CurrentAccountService;

@RestController
@RequestMapping("/accounts")
public class AccountController {

	@Autowired
	CurrentAccountService accountService;
	
	@Autowired
	AccountRepository accountRepo;
	
	@RequestMapping(value = "/me", method = RequestMethod.GET)
	Account getCurrentUser() {
		return accountService.getLoggedInAccount();
	}
	
	@RequestMapping(value = "/me", method = RequestMethod.POST)
	void updateAccount(@RequestBody Account newAccount) {
		
		Account loggedInAccount = accountService.getLoggedInAccount();
		if(!loggedInAccount.getAccountName().equals(newAccount.getAccountName())) {
			throw new NotEnoughPermissionsException("No editing other Accounts!");
		}
		
		Account account = accountRepo.findByAccountName(newAccount.getAccountName());
		
		account = AccountBuilder.startWithExisting(account)
			.withEmail(newAccount.getEmail())
			.withFirstname(newAccount.getFirstname())
			.withLastname(newAccount.getLastname())
			.build();
		
		accountRepo.save(account);
	}
	
	@RequestMapping(value = "/loggedIn", method = RequestMethod.GET)
	void isLoggedIn() {
		
	}
	
}

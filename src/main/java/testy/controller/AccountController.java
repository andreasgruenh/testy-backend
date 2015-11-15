package testy.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import testy.controller.exception.NotEnoughPermissionsException;
import testy.dataaccess.AccountRepository;
import testy.domain.Account;
import testy.domain.util.AccountBuilder;

@RestController
@RequestMapping("/accounts")
public class AccountController extends ApiController{
	
	@Autowired
	AccountRepository accountRepo;
	
	@NeedsLoggedInAccount(admin = "true")
	@RequestMapping(value = "/", method = RequestMethod.GET)
	Iterable<Account> getAllAccounts() {
		return accountRepo.findAll();
	}
	
	@NeedsLoggedInAccount
	@RequestMapping(value = "/me", method = RequestMethod.GET)
	Account getCurrentUser() {
		return loggedInAccount;
	}
	
	@NeedsLoggedInAccount
	@RequestMapping(value = "/me", method = RequestMethod.POST)
	Account updateAccount(@RequestBody Account newAccount) {

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
		return account;
	}
	
	@NeedsLoggedInAccount(admin = "true")
	@RequestMapping(value = "/{id}/isAdmin", method = RequestMethod.PUT)
	Account setIsAdmin(@PathVariable("id") long id, @RequestBody boolean isAdmin) {
		Account account = accountRepo.findById(id);
		account.setAdmin(isAdmin);
		accountRepo.save(account);
		return account;
	}
	
	@RequestMapping(value = "/loggedIn", method = RequestMethod.GET)
	void isLoggedIn() {
		
	}
	
}

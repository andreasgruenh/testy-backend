package testy.security.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import testy.dataaccess.AccountRepository;
import testy.domain.Account;

@Service
public class CreateNewAccountFromLdapService {

	@Autowired
	AccountRepository accountRepository;
	
	public void checkIfAccountIsInDb() {
		String name = SecurityContextHolder.getContext().getAuthentication().getName().toString();
		if(accountRepository.findByAccountName(name) == null) {
			Account newAcc = new Account(name);
			if(name.equals("aroth") || name.equals("issadmin")) {
				newAcc.setAdmin(true);
			}
			accountRepository.save(newAcc);
		}
	}
	
}

package testy.security.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import testy.dataaccess.AccountRepository;
import testy.domain.Account;

@Service
public class CurrentAccountService {

	@Autowired
	AccountRepository accountRepository;
	
	public Account getLoggedInAccount(){
		String name = SecurityContextHolder.getContext().getAuthentication().getName().toString();
		return accountRepository.findByAccountName(name);
	}
}

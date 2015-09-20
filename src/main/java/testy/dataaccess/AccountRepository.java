package testy.dataaccess;

import java.util.Collection;

import org.springframework.data.repository.CrudRepository;

import testy.domain.Account;



public interface AccountRepository extends CrudRepository<Account, Long> {

	Account findByAccountName(String accountName);
	
	Collection<Account> findAll();
	
	Account findById(long id);
	
	Account findByEmail(String email);
	
}

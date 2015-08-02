package testy.dataaccess;

import org.springframework.data.repository.CrudRepository;

import testy.domain.Account;



public interface AccountRepository extends CrudRepository<Account, Long> {

	Account findByAccountName(String accountName);
	
	Iterable<Account> findAll();
	
	Account findById(long id);
	
	Account findByEmail(String email);
	
}

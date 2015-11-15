package testy.helper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import testy.dataaccess.AccountRepository;
import testy.domain.Account;
import testy.domain.util.AccountBuilder;

@Service
public class TestClasses {

	@Autowired
	Environment env;
	
	@Autowired
	AccountRepository accountRepo;
	
	public Account user;
	public String userPassword;
	
	public Account admin;
	public String adminPassword;
	
	public void init() {
		user = AccountBuilder.startWith(env.getProperty("ldap.loginTester"))
				.isAdmin(false)
				.withEmail("andreas.roth@paul-consultants.de")
				.withFirstname("Andreas")
				.withLastname("Roth")
				.build();
		userPassword = env.getProperty("ldap.loginTesterPw");
		
		admin = AccountBuilder.startWith(env.getProperty("ldap.loginAdmin"))
				.isAdmin(true)
				.withEmail("admin@paul-consultants.de")
				.withFirstname("Admin")
				.withLastname("Admin")
				.build();
		adminPassword = env.getProperty("ldap.loginAdminPw");
	}
	
	public void initWithDb() {
		init();
		user = accountRepo.save(user);
		admin = accountRepo.save(admin);
	}
}

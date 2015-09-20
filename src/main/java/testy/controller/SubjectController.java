package testy.controller;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import testy.controller.exception.NotEnoughPermissionsException;
import testy.dataaccess.AccountRepository;
import testy.dataaccess.SubjectRepository;
import testy.domain.Account;
import testy.domain.Subject;
import testy.security.service.CurrentAccountService;

@RestController
@RequestMapping("/subjects")
public class SubjectController {

	@Autowired
	CurrentAccountService accountService;
	
	@Autowired
	AccountRepository accountRepo;
	
	@Autowired
	SubjectRepository subjectRepo;
	
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public Collection<Subject> getAllSubjects() {
		return subjectRepo.findAll();
	}
	
	@RequestMapping(value = "/", method = RequestMethod.POST)
	public Subject createSubject(@RequestBody Subject subject) {
		
		Account loggedInAccount = accountService.getLoggedInAccount();
		if(!loggedInAccount.isAdmin()) {
			throw new NotEnoughPermissionsException("Only admins may create new subjects");
		}
		
		Subject createdSubject = subjectRepo.save(new Subject(subject.getName()));
		return createdSubject;
	}
}

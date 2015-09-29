package testy.controller;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import testy.controller.exception.NotEnoughPermissionsException;
import testy.dataaccess.AccountRepository;
import testy.dataaccess.QuestionPoolRepository;
import testy.dataaccess.SubjectRepository;
import testy.domain.Account;
import testy.domain.Subject;
import testy.domain.test.QuestionPool;
import testy.domain.util.Views;
import testy.security.service.CurrentAccountService;

import com.fasterxml.jackson.annotation.JsonView;

@RestController
@RequestMapping("/subjects")
public class SubjectController {

	@Autowired
	CurrentAccountService accountService;
	
	@Autowired
	AccountRepository accountRepo;
	
	@Autowired
	SubjectRepository subjectRepo;
	
	@Autowired
	QuestionPoolRepository questionPoolRepo;
	
	@JsonView(Views.Summary.class)
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public Collection<Subject> getAllSubjects() {
		return subjectRepo.findAll();
	}
	
	@RequestMapping(value = "/", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.CREATED)
	public Subject createSubject(@RequestBody Subject subject) {
		
		Account loggedInAccount = accountService.getLoggedInAccount();
		if(!loggedInAccount.isAdmin()) {
			throw new NotEnoughPermissionsException("Only admins may create new subjects");
		}
		
		Subject createdSubject = subjectRepo.save(new Subject(subject.getName()));
		return createdSubject;
	}
	
	@JsonView(Views.Summary.class)
	@RequestMapping(value = "/{id}/pools", method = RequestMethod.GET)
	public Collection<QuestionPool> getQuestionPools(@PathVariable("id") long id) {
		return subjectRepo.findById(id).getQuestionPools();
	}
	
	@JsonView(Views.Summary.class)
	@ResponseStatus(HttpStatus.CREATED)
	@RequestMapping(value = "/{id}/pools", method = RequestMethod.POST)
	public QuestionPool createQuestionPool(@PathVariable("id") long id, @RequestBody QuestionPool postedPool) {
		if(!accountService.getLoggedInAccount().isAdmin()) {
			throw new NotEnoughPermissionsException("Only admins may create new question pools");
		}
		QuestionPool newPool = new QuestionPool(postedPool.getName());
		newPool.setPercentageToPass(postedPool.getPercentageToPass());
		Subject subject = subjectRepo.findById(id);
		subject.addQuestionPool(newPool);
		newPool = questionPoolRepo.save(newPool);
		subjectRepo.save(subject);
		return newPool;
	}
}

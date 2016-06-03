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

import testy.dataaccess.AccountRepository;
import testy.dataaccess.QuestionPoolRepository;
import testy.dataaccess.SubjectRepository;
import testy.domain.Subject;
import testy.domain.test.QuestionPool;

@RestController
@RequestMapping("/subjects")
public class SubjectController extends ApiController {
	
	@Autowired
	AccountRepository accountRepo;
	
	@Autowired
	SubjectRepository subjectRepo;
	
	@Autowired
	QuestionPoolRepository questionPoolRepo;
	
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public Collection<Subject> getAllSubjects() {
		return subjectRepo.findAll();
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public Subject getSubjectById(@PathVariable long id) {
		return subjectRepo.findById(id);
	}
	
	@NeedsLoggedInAccount(admin = "true")
	@RequestMapping(value = "/", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.CREATED)
	public Subject createSubject(@RequestBody Subject subject) {
		Subject createdSubject = subjectRepo.save(new Subject(subject.getName()));
		return createdSubject;
	}
	
	@NeedsLoggedInAccount(admin = "true")
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public void deleteSubject(@PathVariable long id) {
		subjectRepo.delete(id);
	}
	
	@NeedsLoggedInAccount(admin = "true")
	@RequestMapping(value = "/{id}", method = RequestMethod.PATCH)
	public Subject updateSubject(@PathVariable("id") long id, @RequestBody Subject changedSubject) {
		Subject oldSubject = subjectRepo.findById(id);
		oldSubject.setDescription(changedSubject.getDescription());
		oldSubject.setName(changedSubject.getName());
		subjectRepo.save(oldSubject);
		return oldSubject;
	}
	
	@RequestMapping(value = "/{id}/pools", method = RequestMethod.GET)
	public Collection<QuestionPool> getQuestionPools(@PathVariable("id") long id) {
		return subjectRepo.findById(id).getQuestionPools();
	}
	
	@NeedsLoggedInAccount(admin = "true")
	@ResponseStatus(HttpStatus.CREATED)
	@RequestMapping(value = "/{id}/pools", method = RequestMethod.POST)
	public QuestionPool createQuestionPool(@PathVariable("id") long id, @RequestBody QuestionPool postedPool) {
		System.out.println("Test");
		QuestionPool newPool = new QuestionPool(postedPool.getName());
		newPool.setPercentageToPass(postedPool.getPercentageToPass());
		Subject subject = subjectRepo.findById(id);
		subject.addQuestionPool(newPool);
		newPool = questionPoolRepo.save(newPool);
		subjectRepo.save(subject);
		return newPool;
	}
}

package testy.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import testy.dataaccess.AccountRepository;
import testy.dataaccess.QuestionPoolRepository;
import testy.dataaccess.TestResultRepository;
import testy.domain.test.TestResult;

@RestController
@RequestMapping("/test-results")
public class TestResultController extends ApiController{
	
	@Autowired
	TestResultRepository testResultRepo;
	
	@Autowired
	AccountRepository accountRepo;
	
	@Autowired
	QuestionPoolRepository questionPoolRepo;
	
	@NeedsLoggedInAccount(admin = "true")
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	void deleteTestResult(@PathVariable long id) {
		TestResult result = testResultRepo.findById(id);
		testResultRepo.delete(result);
		result.getQuestionPool().removeTestResult(result);
		result.getUser().removeTestResult(result);
		accountRepo.save(result.getUser());
		questionPoolRepo.save(result.getQuestionPool());
	}
}

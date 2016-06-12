package testy.controller;

import java.util.Collection;
import java.util.LinkedList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import testy.dataaccess.AccountRepository;
import testy.dataaccess.QuestionPoolRepository;
import testy.dataaccess.TestResultRepository;
import testy.domain.Account;
import testy.domain.test.QuestionPool;
import testy.domain.test.TestResult;

@Component
public class Helper {

	@Autowired
	TestResultRepository testResultRepo;
	
	@Autowired
	AccountRepository accountRepo;
	
	@Autowired
	QuestionPoolRepository questionPoolRepo;
	
	public void deleteTestResult(TestResult result) {
		if (result == null) return;
		QuestionPool pool = result.getQuestionPool();
		Account account = result.getUser();
		pool.removeTestResult(result);
		testResultRepo.delete(result);
		questionPoolRepo.save(pool);
		account.removeTestResult(result);
		accountRepo.save(account);
	}
	
	public void deleteTestResults(Iterable<TestResult> results) {
		Collection<TestResult> allResults = new LinkedList<TestResult>();
		for(TestResult result: results) {
			allResults.add(result);
		}
		for(TestResult result: allResults) {
			deleteTestResult(result);
		}
	}
	
}

package testy.domain.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import testy.TimeService;
import testy.domain.Account;

@Component
public class TestResultFactory {

	@Autowired
	TimeService timeService;
	
	public TestResult createTestResult(Account user, QuestionPool pool, int score) {
		return new TestResult(user, pool, score, timeService.getCurrentTime());
	}
	
}

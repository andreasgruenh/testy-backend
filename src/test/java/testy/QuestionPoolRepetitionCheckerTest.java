package testy;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Calendar;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import testy.domain.Account;
import testy.domain.test.QuestionPool;
import testy.domain.test.TestResult;
import testy.jobs.CheckIfTestIsDue;

public class QuestionPoolRepetitionCheckerTest {
	
	QuestionPool poolWithoutDue = new QuestionPool("Test without dueDate");
	QuestionPool poolWithDue = new QuestionPool("Test with dueDate");
	Account account = new Account("Some user");
	TestResult resultInDuePool;
	TestResult result;

	long resultStamp = System.currentTimeMillis();
	Calendar calendar = Calendar.getInstance();
	long twoWeeksAfterResult;
	long fourWeeksAfterResult;
	
	@Mock
	private TimeService timeServiceMock;

	@InjectMocks
	private CheckIfTestIsDue checker;
	
	@Before
	public void setUp() {
		checker = new CheckIfTestIsDue();
		calendar.setTimeInMillis(resultStamp);
		calendar.add(Calendar.DAY_OF_YEAR, 14);
		twoWeeksAfterResult = calendar.getTimeInMillis();
		calendar.add(Calendar.DAY_OF_YEAR, 14);
		fourWeeksAfterResult = calendar.getTimeInMillis();
		poolWithDue.setWeeksAfterWhichTestHasToBeRepeated(3);
		resultInDuePool = new TestResult(account, poolWithDue, 100, resultStamp);
		result = new TestResult(account, poolWithoutDue, 100, resultStamp);

	    MockitoAnnotations.initMocks(this);
	}

	@Test
	public void IsTestDue_ShouldReturnTrueIfTestIsDue() {
		// arrange
	    Mockito.when(timeServiceMock.getCurrentTime()).thenReturn(fourWeeksAfterResult);   
	    
		assertTrue(checker.isTestDue(resultInDuePool));
	}
	
	@Test
	public void IsTestDue_ShouldReturnFalseIfTestIsNotYetDue() {
		// arrange
	    Mockito.when(timeServiceMock.getCurrentTime()).thenReturn(twoWeeksAfterResult);   
	    
		assertFalse(checker.isTestDue(resultInDuePool));
	}
	
	@Test
	public void IsTestDue_ShouldReturnFalseIfQuestionPoolHasNoDue() {
		// arrange
	    Mockito.when(timeServiceMock.getCurrentTime()).thenReturn(twoWeeksAfterResult);   
	    
		assertFalse(checker.isTestDue(result));
	}
	
}

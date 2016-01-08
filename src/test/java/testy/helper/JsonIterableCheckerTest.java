package testy.helper;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import testy.domain.Account;
import testy.domain.Subject;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonIterableCheckerTest {
	
	private ObjectMapper mapper = new ObjectMapper();
	
	private Account[] accountArray = new Account[]{new Account("Account Name")};
	private Subject[] subjectArray = new Subject[]{new Subject("Subject Name")};
	
	private String accountString;
	private String subjectString;
	
	@Before
	public void setUp() throws JsonProcessingException {
		accountString = mapper.writeValueAsString(accountArray);
		subjectString = mapper.writeValueAsString(subjectArray);
	}
	
	@Test
	public void representsCollectionOfClass_shouldReturnFalseWhenWrongStringIsGiven() {
		// act
		boolean representsCollectionofClass = JsonIterableChecker.representsIterableOfClass(accountString, Subject.class);
		
		// assert
		assertFalse("method should return false!", representsCollectionofClass);
	}
	
	@Test
	public void representsCollectionOfClass_shouldAlsoReturnFalseWhenWrongStringIsGiven() {
		// act
		boolean representsCollectionofClass = JsonIterableChecker.representsIterableOfClass(subjectString, Account.class);
		
		// assert
		assertFalse("method should return false!", representsCollectionofClass);
	}
	
	@Test
	public void representsCollectionOfClass_shouldReturnTrueWhenCorrectStringIsGiven() {
		// act
		boolean representsCollectionofClass = JsonIterableChecker.representsIterableOfClass(accountString, Account.class);
		
		// assert
		assertTrue("method should return true!", representsCollectionofClass);
	}
	
	@Test
	public void representsCollectionOfClass_shouldAlsoReturnTrueWhenCorrectStringIsGiven() {
		// act
		boolean representsCollectionofClass = JsonIterableChecker.representsIterableOfClass(subjectString, Subject.class);
		
		// assert
		assertTrue("method should return true!", representsCollectionofClass);
	}

}

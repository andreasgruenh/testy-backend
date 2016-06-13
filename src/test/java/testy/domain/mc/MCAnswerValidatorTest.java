package testy.domain.mc;

import static org.junit.Assert.assertTrue;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashSet;

import org.junit.Before;
import org.junit.Test;

import testy.domain.question.mc.MCAnswer;
import testy.domain.question.mc.MCAnswerValidator;
import testy.domain.question.mc.MCPossibility;
import testy.domain.question.mc.MCQuestion;

public class MCAnswerValidatorTest {

	MCAnswerValidator validator = new MCAnswerValidator();

	private MCQuestion quest1 = new MCQuestion();
	private MCQuestion quest2 = new MCQuestion();
	private MCQuestion quest3 = new MCQuestion();

	private MCPossibility poss1 = new MCPossibility("Frage?", true);
	private MCPossibility poss2 = new MCPossibility("Frage?", false);
	private MCPossibility poss3 = new MCPossibility("Frage?", false);

	private MCPossibility poss4 = new MCPossibility("Frage?", true);
	private MCPossibility poss5 = new MCPossibility("Frage?", true);
	private MCPossibility poss6 = new MCPossibility("Frage?", true);

	private MCPossibility poss7 = new MCPossibility("Frage?", true);
	private MCPossibility poss8 = new MCPossibility("Frage?", true);
	private MCPossibility poss9 = new MCPossibility("Frage?", false);
	private MCPossibility poss10 = new MCPossibility("Frage?", false);

	private MCAnswer answer1 = new MCAnswer(quest1,
	        new HashSet<MCPossibility>(Arrays.asList(poss1)), new HashSet<MCPossibility>(
	                Arrays.asList(poss2, poss3)));
	private MCAnswer answer2 = new MCAnswer(quest2,
	        new HashSet<MCPossibility>(Arrays.asList(poss4)), new HashSet<MCPossibility>(
	                Arrays.asList(poss5, poss6)));
	private MCAnswer answer3 = new MCAnswer(quest3, new HashSet<MCPossibility>(Arrays.asList(poss9,
	        poss10)), new HashSet<MCPossibility>(Arrays.asList(poss7, poss8)));

	@Before
	public void setup() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		setId(quest1, 1);
		setId(poss1, 1);
		quest1.addAnswer(poss1);
		setId(poss2, 2);
		quest1.addAnswer(poss2);
		setId(poss3, 3);
		quest1.addAnswer(poss3);

		setId(quest2, 2);
		setId(poss4, 4);
		quest2.addAnswer(poss4);
		setId(poss5, 5);
		quest2.addAnswer(poss5);
		setId(poss6, 6);
		quest2.addAnswer(poss6);

		setId(quest3, 3);
		setId(poss7, 7);
		quest3.addAnswer(poss7);
		setId(poss8, 8);
		quest3.addAnswer(poss8);
		setId(poss9, 9);
		quest3.addAnswer(poss9);
		setId(poss10, 10);
		quest3.addAnswer(poss10);
	}

	@Test
	public void validate_shouldReturnMaxScoreIfEverythingIsCorrect() {
		int score = validator.validate(answer1);
		System.out.println(quest1.getId());

		assertTrue("Validate should return maxScore if everything is correct. It was " + score
		        + " but expected is: " + quest1.getMaxScore(), score == quest1.getMaxScore());
	}

	@Test
	public void validate_shouldReturnZeroPointsIfEverythingIsWrong() {
		int score = validator.validate(answer3);
		assertTrue("Validate should return zero if Everything is wron. It was " + score
		        + " but expected is: 0", score == 0);
	}

	@Test
	public void validate_shouldReturnValueBetweenZeroAndMaxScoreIfNotEverythingIsCorrect() {
		int score = validator.validate(answer2);
		assertTrue(
		        "Validate should return value between zero and maxScore if not everthing was correct. It was "
		                + score + " but expected is "
		                + (int) (quest2.getMaxScore() * (double) 1 / 3),
		        score == (int) (quest2.getMaxScore() * (double) 1 / 3));
	}
	
	private void setId(Object object, long id) throws IllegalArgumentException, IllegalAccessException {
		Field idField = findField(object, "id");
		boolean wasAccessible = idField.isAccessible();
		idField.setAccessible(true);
		idField.set(object, id);
		idField.setAccessible(wasAccessible);
	}
	
	private Field findField(Object o, String fieldName) {
		Field field = null;
		Class<?> currentClass = o.getClass();
		while(field == null) {
			try {
				field = currentClass.getDeclaredField(fieldName);
			} catch(Exception e) {
				if (currentClass == null) break;
				currentClass = currentClass.getSuperclass();
			}
		}
		return field;
	}

}

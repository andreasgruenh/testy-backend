package testy.domain.mc;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.HashSet;

import org.junit.Before;
import org.junit.Test;

import testy.domain.question.mc.MCAnswer;
import testy.domain.question.mc.MCPossibility;
import testy.domain.question.mc.MCQuestion;
import testy.domain.question.mc.MCAnswerValidator;

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
	private MCAnswer answer3 = new MCAnswer(quest3, new HashSet<MCPossibility>(),
	        new HashSet<MCPossibility>(Arrays.asList(poss7, poss8, poss9, poss10)));

	@Before
	public void setup() {
		quest1.addAnswer(poss1);
		quest1.addAnswer(poss2);
		quest1.addAnswer(poss3);

		quest2.addAnswer(poss4);
		quest2.addAnswer(poss5);
		quest2.addAnswer(poss6);

		quest3.addAnswer(poss7);
		quest3.addAnswer(poss8);
		quest3.addAnswer(poss9);
		quest3.addAnswer(poss10);
	}

	@Test
	public void validate_shouldReturnMaxScoreIfEverythingIsCorrect() {
		int score = validator.validate(answer1);

		assertTrue("Validate should return maxScore if everything is correct. It was " + score
		        + " but expected is: " + quest1.getMaxScore(), score == quest1.getMaxScore());
	}

	@Test
	public void validate_shouldReturnZeroPointsIfNothingIsChecked() {
		int score = validator.validate(answer3);
		assertTrue("Validate should return zero if nothing is checked. It was " + score
		        + " but expected is: 0", score == 0);
	}

	@Test
	public void validate_shouldReturnValueBetweenZeroAndMaxScoreIfNotEverythingIsCorrect() {
		int score = validator.validate(answer2);
		assertTrue(
		        "Validate should return value between zero and maxScore if not everthing was correct. It was "
		                + score + " but expected is something between 0 and "
		                + answer2.getQuestion().getMaxScore(), score > 0
		                && score < answer2.getQuestion().getMaxScore());
	}

}

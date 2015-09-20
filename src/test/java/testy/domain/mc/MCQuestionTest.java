package testy.domain.mc;

import static org.junit.Assert.*;
import static testy.helper.UnmodifiableChecker.isUnmodifiable;

import org.junit.Before;
import org.junit.Test;

import testy.domain.question.Category;
import testy.domain.question.mc.MCQuestion;

public class MCQuestionTest {

	Category category = new Category("First Category");
	MCQuestion quest1 = new MCQuestion();
	MCQuestion quest2 = new MCQuestion();
	MCQuestion quest3 = new MCQuestion();
	MCQuestion quest4 = new MCQuestion();
	
	@Before
	public void setup() {
		category.addQuestion(quest4);
	}
	
	@Test
	public void setCategory_shoulSetCategoryAndAddQuestionToCategory() {
		quest1.setCategory(category);
		assertTrue("Category should be set", quest1.getCategory() == category);
		assertTrue("Question should be added to category", category.getQuestions().contains(quest1));
	}
	
	public void unsetCategory_shouldSetCategoryToNullAndRemoveQuestionFromCategory() {
		quest4.unsetCategory();
		assertTrue("Category should be null", quest4.getCategory() == null);
		assertFalse("Question should be added to category", category.getQuestions().contains(quest4));
	}
	
	@Test
	public void getPossibleAnswers_shouldReturnUnmodifiableSet() {
		assertTrue("Set should be unmodifiable", isUnmodifiable(quest1.getPossibleAnswers()));
	}

}

package testy.domain;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import testy.domain.question.AbstractQuestion;
import testy.domain.question.Category;
import testy.domain.question.mc.MCQuestion;

public class CategoryTest {

	Category category = new Category("First Category");
	MCQuestion quest1 = new MCQuestion();
	MCQuestion quest2 = new MCQuestion();
	MCQuestion quest3 = new MCQuestion();
	MCQuestion quest4 = new MCQuestion();
	
	@Before
	public void setup() {
		category.addQuestion(quest4);
	}
	
	@Test(expected=NullPointerException.class)
	public void addQuestion_shouldThrowNPE_whenNullIsPassed() {
		category.addQuestion(null);
	}
	
	@Test
	public void addQuestion_ShouldAddQuestionToCategoryAndCategoryToQuestion() {
		category.addQuestion(quest1);
		assertTrue("Category should contain Question", category.getQuestions().contains(quest1));
		assertTrue("Question should have correct category", quest1.getCategory() == category);
	}
	
	@Test
	public void addAllQuestions_shouldAddAllQuestionsWithCorrectCategories() {
		Set<AbstractQuestion> questions = new HashSet<AbstractQuestion>();
		questions.add(quest2);
		questions.add(quest3);
		category.addAllQuestions(questions);
		
		assertTrue("Category should contain Questions", category.getQuestions().containsAll(questions));
		assertTrue("Questions should have correct category", quest2.getCategory() == category && quest3.getCategory() == category);
	}
	
	@Test
	public void removeQuestion_shouldRemoveQuestionAndUnsetCategoryOfQuestion() {
		category.removeQuestion(quest4);
		assertFalse("Question should no longer be in category", category.getQuestions().contains(quest4));
		assertTrue("Category of question should be null", quest4.getCategory() == null);
	}
	
	@Test(expected=NullPointerException.class)
	public void removeQuestion_shouldThrowNPE_whenNullIsPassed() {
		category.removeQuestion(null);
	}
	
	@Test(expected=UnsupportedOperationException.class)
	public void getQuestions_shouldReturnUnmodifableSet() {
		MCQuestion question1 = new MCQuestion();
		MCQuestion question2 = new MCQuestion();
		category.addQuestion(question1);
		
		category.getQuestions().add(question2);
		
	}

}

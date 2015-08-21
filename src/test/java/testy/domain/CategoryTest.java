package testy.domain;

import org.junit.Test;

import testy.domain.question.Category;
import testy.domain.question.mc.MCQuestion;

public class CategoryTest {

	Category category = new Category("First Category");
	
	@Test(expected=NullPointerException.class)
	public void addQuestion_shouldThrowNPE_whenNullIsPassed() {
		category.addQuestion(null);
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

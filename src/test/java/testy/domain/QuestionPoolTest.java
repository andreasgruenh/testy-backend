package testy.domain;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import testy.domain.question.Category;
import testy.domain.question.mc.MCQuestion;
import testy.domain.test.QuestionPool;

public class QuestionPoolTest {

	QuestionPool pool = new QuestionPool();
	Category category1 = new Category("First Category");
	Category category2 = new Category("Second Category");
	MCQuestion question1 = new MCQuestion();
	MCQuestion question2 = new MCQuestion();
	
	@Before
	public void setup() {
		category1.setMaxScore(20);
		category2.setMaxScore(20);
		
		category1.addQuestion(question1);
		category2.addQuestion(question2);
		
		pool.addCategory(category1);
		pool.addCategory(category2);
	}
	
	@Test
	public void getMaxScoreOfConcreteTest_shouldReturnCorrectScore() {
		assertTrue("MaxScoreOfConcreteTest is not calculated properly", pool.getMaxScoreOfConcreteTest() == 40);
	}
	
	@Test(expected=UnsupportedOperationException.class)
	public void getCategories_shouldReturnUnmodifableSet() {
		pool.getCategories().add(null);
	}
	
	@Test(expected=NullPointerException.class)
	public void addCategory_shouldThrowNPE_whenNullIsPassed() {
		pool.addCategory(null);
	}
	
	@Test(expected=NullPointerException.class)
	public void removeCategory_shouldThrowNPE_whenNullIsPassed() {
		pool.removeCategory(null);
	}

}

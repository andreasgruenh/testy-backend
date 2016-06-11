package testy.domain;

import static org.junit.Assert.assertTrue;
import static testy.helper.UnmodifiableChecker.isUnmodifiable;

import org.junit.Before;
import org.junit.Test;

import testy.domain.question.mc.MCQuestion;
import testy.domain.test.Category;
import testy.domain.test.QuestionPool;

public class QuestionPoolTest {

	QuestionPool pool = new QuestionPool();
	Category category1 = new Category("First Category");
	Category category2 = new Category("Second Category");
	MCQuestion question1 = new MCQuestion();
	MCQuestion question2 = new MCQuestion();
	
	Subject subject = new Subject();
	
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
	
	@Test
	public void getCategories_shouldReturnUnmodifableSet() {
			assertTrue("Set should be unmodifiable", isUnmodifiable(pool.getCategories()));
	}
	
	@Test(expected=NullPointerException.class)
	public void addCategory_shouldThrowNPE_whenNullIsPassed() {
		pool.addCategory(null);
	}
	
	@Test
	public void addCategory_shouldAddCategoryAndUpdatePoolOfCategory() {
		
		// arrange
		Category newCat = new Category();
		
		// act
		pool.addCategory(newCat);
		
		// assert
		assertTrue("Category should be added", pool.getCategories().contains(newCat));
		assertTrue("Pool of category should be set", newCat.getPool() == pool);
	}
	
	@Test(expected=NullPointerException.class)
	public void removeCategory_shouldThrowNPE_whenNullIsPassed() {
		pool.removeCategory(null);
	}
	
	@Test
	public void removeCategory_shouldRemoveCategoryAndUnsetPoolOfCategory() {
		// act
		pool.removeCategory(category1);
		
		// assert
		assertTrue("Category should be removed", !pool.getCategories().contains(category1));
		assertTrue("Category should have no pool", category1.getPool() == null);
	}
	
	@Test
	public void setSubject_shouldSetSubjectAndAddPoolToSubject() {
		pool.setSubject(subject);
		assertTrue("Subject should be set", pool.getSubject() == subject);
		assertTrue("Pool should be in subjects pools", subject.getQuestionPools().contains(pool));
	}

}

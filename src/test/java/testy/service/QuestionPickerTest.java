package testy.service;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import testy.domain.question.AbstractQuestion;
import testy.domain.question.mc.MCQuestion;
import testy.domain.test.Category;
import testy.domain.test.QuestionPicker;
import testy.domain.test.QuestionPool;

public class QuestionPickerTest {

	int questionCount = 30;
	
	QuestionPicker picker = new QuestionPicker();
	QuestionPool pool = new QuestionPool();
	Category category1 = new Category("First Category");
	Category category2 = new Category("Second Category");
	Category category3 = new Category("Third Category");
	MCQuestion[] questions = new MCQuestion[questionCount];

	@Before
	public void setup() {
		int index = 0;
		for (MCQuestion question : questions) {
			question = new MCQuestion();
			if (index % 3 == 0) {
				category1.addQuestion(question);
			} else if (index % 3 == 1) {
				category2.addQuestion(question);
			} else {
				category3.addQuestion(question);
			}
			index++;
		}

		category1.setMaxScore(30);
		category2.setMaxScore(30);
		category3.setMaxScore(30);
		pool.addCategory(category1);
		pool.addCategory(category2);
		pool.addCategory(category3);
	}

	@Test
	public void setupMethod_shouldFillPoolCorrectly() {
		for (Category cat : pool.getCategories()) {
			assertTrue("Each Category should Contain 5 Questions", cat.getQuestions().size() == questionCount / 3);
		}
	}

	@Test
	public void generateCategories_shouldReturnCategoriesWithCorrectTotalScore() {
		testy.domain.test.Test test = picker.generateTestWithRandomQuestionsFrom(pool);
		int totalScore = 0;

		for (Category cat : test.getCategories()) {
			int scoreOfCategory = 0;
			for (AbstractQuestion question : cat.getQuestions()) {
				scoreOfCategory += question.getMaxScore();
			}
			totalScore += scoreOfCategory;
		}

		assertTrue("TotalScore of Test should be equal to Max Score of Categories, it was"
		        + totalScore + "but " + pool.getMaxScoreOfConcreteTest() + "was expected",
		        totalScore == pool.getMaxScoreOfConcreteTest());
	}

}

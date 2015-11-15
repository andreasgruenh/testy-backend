package testy.domain.test;

import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;

import testy.domain.question.AbstractQuestion;

@Service
public class QuestionPicker {

	/* 
	 * Returns the Category with random questions from the original catgegories
	 */
	public Set<Category> generateCategoriesWithRandomQuestionsFrom(QuestionPool pool) {
		
		Set<Category> categories =  pool.getCategories();
		Set<Category> resultSet = new HashSet<Category>();
		
		for(Category cat: categories) {
			Category strippedCat = new Category();
			strippedCat.addAllQuestions(getRandomQuestionsFrom(cat));
			resultSet.add(strippedCat);
		}
		return resultSet;
	};
	
	private Set<AbstractQuestion> getRandomQuestionsFrom(Category cat) {
		
		int maxScore = cat.getMaxScore();
		
		Set<AbstractQuestion> resultSet = new HashSet<AbstractQuestion>();
		
		List<AbstractQuestion> allQuestions = new LinkedList<AbstractQuestion>();
		allQuestions.addAll(cat.getQuestions());
		Collections.shuffle(allQuestions);
		
		while(sumOfScores(resultSet) < maxScore) {
			if(sumOfScores(resultSet) + allQuestions.get(0).getMaxScore() <= maxScore) {
				resultSet.add(allQuestions.remove(0));
			} else {
				allQuestions.remove(0);
			}
		}
		
		return resultSet;
	}
	
	private int sumOfScores(Iterable<AbstractQuestion> questions) {
		int result = 0;
		for(AbstractQuestion question: questions) {
			result += question.getMaxScore();
		}
		return result;
	}
	
}

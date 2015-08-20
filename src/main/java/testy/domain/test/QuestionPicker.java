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

	public Set<AbstractQuestion<?, ?>> pickQuestionsFrom(QuestionPool pool) {
		
		int maxScore = pool.getMaxScoreOfConcreteTest();
		
		List<AbstractQuestion<?, ?>> questions = new LinkedList<AbstractQuestion<?, ?>>();
		questions.addAll(pool.getQuestions());
		
		Collections.shuffle(questions);
		
		Set<AbstractQuestion<?, ?>> result = new HashSet<AbstractQuestion<?, ?>>();
		
		while(sumOfScores(result) < maxScore) {
			if(sumOfScores(result) + questions.get(0).getMaxScore() <= maxScore) {
				result.add(questions.remove(0));
			} else {
				questions.remove(0);
			}
		}
		
		return result;
	};
	
	private int sumOfScores(Iterable<AbstractQuestion<?, ?>> questions) {
		int result = 0;
		for(AbstractQuestion<?, ?> question: questions) {
			result += question.getMaxScore();
		}
		return result;
	}
	
}

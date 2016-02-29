package testy.domain.test;

import org.springframework.stereotype.Service;

import testy.domain.question.AbstractAnswer;

@Service
public class TestValidator {

	public int validateTest(AbstractAnswer<?>[] answers) {
		int score = 0;

		for(AbstractAnswer<?> answer: answers) {
			score += answer.validate();
		}
		
		return score;
	}
	
}

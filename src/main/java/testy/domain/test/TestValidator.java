package testy.domain.test;

import java.lang.reflect.Field;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import testy.dataaccess.QuestionRepository;
import testy.domain.question.AbstractAnswer;
import testy.domain.question.AbstractQuestion;

@Service
public class TestValidator {

	@Autowired
	QuestionRepository questionRepo;
	
	public int validateTest(AbstractAnswer<?>[] answers) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		int score = 0;

		for(AbstractAnswer<?> answer: answers) {
			long questionId = answer.getQuestion().getId();
			AbstractQuestion realQuestion = questionRepo.findById(questionId);
			Field[] fields = answer.getClass().getSuperclass().getDeclaredFields();
			for(Field field: fields) {
				if (field.getName().equals("question")) {
					boolean wasAccessible = field.isAccessible();
					field.setAccessible(true);
					field.set(answer, realQuestion);
					field.setAccessible(wasAccessible);
				}
			}
			score += answer.validate();
		}
		
		return score;
	}
	
}

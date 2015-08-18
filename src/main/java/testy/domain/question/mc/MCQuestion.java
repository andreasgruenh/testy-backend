package testy.domain.question.mc;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;

import testy.domain.question.AbstractQuestion;

@Entity
public class MCQuestion extends AbstractQuestion<MCQuestion, MCAnswer> {

	private static MCQuestionValidator validator = new MCQuestionValidator();
	
	@ElementCollection
	private Set<String> answers;
	
	public MCQuestion() {
		answers = new HashSet<String>();
	}
	
	public void setAnswers(Set<String> answers) {
		this.answers = answers;
	}
}

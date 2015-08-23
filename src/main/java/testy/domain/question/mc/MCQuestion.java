package testy.domain.question.mc;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.OneToMany;

import testy.domain.question.AbstractQuestion;
import testy.domain.question.QuestionType;

@Entity
public class MCQuestion extends AbstractQuestion<MCQuestion, MCAnswer> {
	
	@OneToMany
	private Set<MCPossibility> possibleAnswers;
	
	public MCQuestion() {
		possibleAnswers = new HashSet<MCPossibility>();
		type = QuestionType.MCQuestion;
	}
	
	public void addAnswer(MCPossibility answer) {
		if(answer == null) {
			throw new NullPointerException();
		}
		possibleAnswers.add(answer);
		type = QuestionType.MCQuestion;
	}
	
	public void removeAnswer(MCPossibility answer) {
		if(answer == null) {
			throw new NullPointerException();
		}
		possibleAnswers.remove(answer);
	}
	
	public void updateAnswer(MCPossibility answer) {
		if(answer == null) {
			throw new NullPointerException();
		}
		possibleAnswers.remove(answer);
		possibleAnswers.add(new MCPossibility(answer.getText(), answer.isCorrect()));
	}
}

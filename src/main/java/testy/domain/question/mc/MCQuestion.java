package testy.domain.question.mc;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.OneToMany;

import testy.domain.question.AbstractQuestion;

import com.fasterxml.jackson.annotation.JsonTypeName;

@Entity
@JsonTypeName("MCQuestion")
public class MCQuestion extends AbstractQuestion {
	
	@OneToMany
	private Set<MCPossibility> possibleAnswers;
	
	public MCQuestion() {
		possibleAnswers = new HashSet<MCPossibility>();
	}
	
	public void addAnswer(MCPossibility answer) {
		if(answer == null) {
			throw new NullPointerException();
		}
		possibleAnswers.add(answer);
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

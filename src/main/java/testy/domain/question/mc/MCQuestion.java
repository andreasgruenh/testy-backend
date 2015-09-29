package testy.domain.question.mc;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.OneToMany;

import testy.domain.question.AbstractQuestion;
import testy.domain.util.Views;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.annotation.JsonView;

@Entity
@JsonTypeName("MCQuestion")
public class MCQuestion extends AbstractQuestion {
	
	@OneToMany
	@JsonView(Views.Summary.class)
	private Set<MCPossibility> possibleAnswers = new HashSet<MCPossibility>();
	
	public MCQuestion() {
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
	
	public Set<MCPossibility> getPossibleAnswers() {
		return Collections.unmodifiableSet(possibleAnswers);
	}
}

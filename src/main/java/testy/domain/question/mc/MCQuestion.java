package testy.domain.question.mc;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import testy.domain.question.AbstractQuestion;

@Entity
public class MCQuestion extends AbstractQuestion<MCQuestion, MCAnswer> {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
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

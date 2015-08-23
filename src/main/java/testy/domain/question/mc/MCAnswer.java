package testy.domain.question.mc;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import testy.domain.question.AbstractAnswer;

@Entity
public class MCAnswer extends AbstractAnswer {
	
	private static MCAnswerValidator validator = new MCAnswerValidator();
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	private final MCQuestion question;
	
	@OneToMany
	private final Set<MCPossibility> checkedPossibilities;
	
	@OneToMany
	private final Set<MCPossibility> uncheckedPossibilities;
	
	public MCAnswer(MCQuestion question, Set<MCPossibility> checkedAnswers, Set<MCPossibility> uncheckedAnswers) {
		this.question = question;
		this.checkedPossibilities = checkedAnswers;
		this.uncheckedPossibilities = uncheckedAnswers;
	}

	public long getId() {
		return id;
	}

	public MCQuestion getQuestion() {
		return question;
	}

	public Set<MCPossibility> getCheckedPossibilities() {
		return checkedPossibilities;
	}

	public Set<MCPossibility> getUncheckedPossibilities() {
		return uncheckedPossibilities;
	}
	
	@Override
	public int validate() {
		return validator.validate(this);
	}
}

package testy.domain.question.mc;

import java.util.Collections;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.OneToMany;

import testy.domain.question.AbstractAnswer;

import com.fasterxml.jackson.annotation.JsonTypeName;

@Entity
@JsonTypeName("MCAnswer")
public class MCAnswer extends AbstractAnswer<MCQuestion> {
	
	private static MCAnswerValidator validator = new MCAnswerValidator();
	
	@OneToMany
	private Set<MCPossibility> checkedPossibilities;
	
	@OneToMany
	private Set<MCPossibility> uncheckedPossibilities;
	
	@Deprecated
	public MCAnswer() {
	}
	
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
		return Collections.unmodifiableSet(checkedPossibilities);
	}

	public Set<MCPossibility> getUncheckedPossibilities() {
		return Collections.unmodifiableSet(uncheckedPossibilities);
	}
	
	@Override
	public int validate() {
		return validator.validate(this);
	}
}

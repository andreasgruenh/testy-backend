package testy.domain.question.mc;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import testy.domain.question.AbstractAnswer;

@Entity
public class MCAnswer extends AbstractAnswer {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	private final MCQuestion question;
	
	private final boolean checked;
	
	public MCAnswer(MCQuestion question, boolean checked) {
		this.question = question;
		this.checked = checked;
	}

	public long getId() {
		return id;
	}

	public MCQuestion getQuestion() {
		return question;
	}

	public boolean isChecked() {
		return checked;
	}
}

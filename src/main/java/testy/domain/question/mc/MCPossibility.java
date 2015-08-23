package testy.domain.question.mc;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class MCPossibility {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	private final String text;
	
	private final boolean isCorrect;
	
	public MCPossibility(String text, boolean isCorrect) {
		this.text = text;
		this.isCorrect = isCorrect;
	}

	public String getText() {
		return text;
	}

	public boolean isCorrect() {
		return isCorrect;
	}

	public long getId() {
		return id;
	}
}

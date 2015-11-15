package testy.domain.question.mc;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import testy.domain.util.Views;

import com.fasterxml.jackson.annotation.JsonView;

@Entity
public class MCPossibility {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@JsonView(Views.Summary.class)
	private long id;
	
	@JsonView(Views.Summary.class)
	private final String text;
	
	@JsonView(Views.Summary.class)
	private final boolean isCorrect;
	
	@Deprecated
	public MCPossibility() {
		text = "";
		isCorrect = false;
	}
	
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

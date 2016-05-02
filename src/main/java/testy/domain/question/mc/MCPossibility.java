package testy.domain.question.mc;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonView;

import testy.domain.util.Views.Test;

@Entity
public class MCPossibility {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	private final String text;
	
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

	@JsonView(Test.class)
	public boolean isCorrect() {
		return isCorrect;
	}

	public long getId() {
		return id;
	}
}

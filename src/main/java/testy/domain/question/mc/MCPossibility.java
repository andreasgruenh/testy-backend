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
	@JsonView(Views.NoCircleView.class)
	private long id;
	
	@JsonView(Views.NoCircleView.class)
	private final String text;
	
	@JsonView(Views.NoCircleView.class)
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

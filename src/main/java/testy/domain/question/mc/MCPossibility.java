package testy.domain.question.mc;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;

import testy.domain.util.Views.Admin;
import testy.domain.util.Views.Summary;
import testy.domain.util.Views.Test;

import com.fasterxml.jackson.annotation.JsonView;

@Entity
public class MCPossibility {

	@Id
	@JsonView(Summary.class)
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	@Lob
	@JsonView(Summary.class)
	private final String text;
	
	@JsonView(Admin.class)
	private final boolean correct;
	
	@Deprecated
	public MCPossibility() {
		text = "";
		correct = false;
	}
	
	public MCPossibility(String text, boolean isCorrect) {
		this.text = text;
		this.correct = isCorrect;
	}

	public String getText() {
		return text;
	}

	@JsonView(Test.class)
	public boolean isCorrect() {
		return correct;
	}

	public long getId() {
		return id;
	}
}

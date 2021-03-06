package testy.domain.question;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToOne;

import testy.domain.question.image.ImageQuestion;
import testy.domain.question.mc.MCQuestion;
import testy.domain.test.Category;
import testy.domain.util.Views.Summary;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.annotation.JsonView;

@Entity
@JsonTypeInfo(use=JsonTypeInfo.Id.NAME, include = As.PROPERTY, property = "type")
@JsonSubTypes({
    @Type(name="MCQuestion", value=MCQuestion.class), 
    @Type(name="ImageQuestion", value=ImageQuestion.class)})
public abstract class AbstractQuestion {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@JsonView(Summary.class)
	protected long id;
	
	@JsonIgnoreProperties({ "questions" })
	@JsonView(Summary.class)
	@OneToOne
	private Category category;
	
	@Lob
	@JsonView(Summary.class)
	protected String questionString;
	
	@JsonView(Summary.class)
	protected final int maxScore = 10;
	
	public long getId() {
		return id;
	}
	
	public String getQuestionString() {
		return questionString;
	}

	public void setQuestionString(String s) {
		if(s == null) {
			throw new NullPointerException();
		}
		if(s.length() == 0) {
			throw new IllegalArgumentException();
		}
		questionString = s;
	}
	
	public int getMaxScore() {
		return maxScore;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
		if(!category.getQuestions().contains(this)) {
			category.addQuestion(this);
		}
	}
	
	public void unsetCategory() {
		if(this.category.getQuestions().contains(this)) {
			category.removeQuestion(this);
		}
		this.category = null;
	}
}

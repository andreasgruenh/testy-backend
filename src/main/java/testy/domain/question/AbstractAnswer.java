package testy.domain.question;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import testy.domain.question.image.ImageAnswer;
import testy.domain.question.mc.MCAnswer;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;

@JsonTypeInfo(use=JsonTypeInfo.Id.NAME, include = As.PROPERTY, property = "type")
@JsonSubTypes({
    @Type(name="MCAnswer", value=MCAnswer.class), 
    @Type(name="ImageAnswer", value=ImageAnswer.class)})
@Entity
public abstract class AbstractAnswer<Q extends AbstractQuestion> {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	protected long id;
	
	@OneToOne
	protected Q question;
	
	public abstract int validate();
	
	public Q getQuestion() {
		return question;
	}
}

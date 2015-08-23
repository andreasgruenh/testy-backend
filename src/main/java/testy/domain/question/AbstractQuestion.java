package testy.domain.question;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
public abstract class AbstractQuestion<Q extends AbstractQuestion<Q, A>, A> {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	protected long id;
	
	@Enumerated(EnumType.STRING)
	protected QuestionType type;
	
	@OneToOne
	private Category category;
	
	protected String questionString;
	
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
}

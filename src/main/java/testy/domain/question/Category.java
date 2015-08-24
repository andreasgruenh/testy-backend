package testy.domain.question;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Category {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	protected long id;
	
	private String name;
	
	private int maxScore;
	
	@OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
	private Set<AbstractQuestion> questions;
	
	public Category() {
		questions = new HashSet<AbstractQuestion>();
	}
	
	public Category(String name) {
		questions = new HashSet<AbstractQuestion>();
		this.name = name;
	}
	
	public int getMaxScore() {
		return maxScore;
	}

	public void setMaxScore(int score) {
		this.maxScore = score;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getId() {
		return id;
	}

	public Set<AbstractQuestion> getQuestions() {
		return Collections.unmodifiableSet(questions);
	}
	
	public void addQuestion(AbstractQuestion question) {
		if(question == null) {
			throw new NullPointerException();
		}
		questions.add(question);
		if(question.getCategory() != this) {
			question.setCategory(this);
		}
	}
	
	public void addAllQuestions(Collection<AbstractQuestion> questions) {
		if(questions == null) {
			throw new NullPointerException();
		}
		this.questions.addAll(questions);
		for(AbstractQuestion question: questions) {
			if(question.getCategory() != this) {
				question.setCategory(this);
			}
		}
	}
	
	public void removeQuestion(AbstractQuestion question) {
		if(question == null) {
			throw new NullPointerException();
		}
		questions.remove(question);
		if(question.getCategory() != null) {
			question.setCategory(null);
		}
	}
	
}

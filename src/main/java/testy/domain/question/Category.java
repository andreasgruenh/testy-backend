package testy.domain.question;

import java.util.Collections;
import java.util.Set;

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
	
	@OneToMany
	private Set<AbstractQuestion<?, ?>> questions;
	
	public Category() {
		
	}
	
	public Category(String name) {
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

	public Set<AbstractQuestion<?, ?>> getQuestions() {
		return Collections.unmodifiableSet(questions);
	}
	
	
	
}

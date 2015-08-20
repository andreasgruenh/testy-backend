package testy.domain.test;

import java.util.Collections;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import testy.domain.question.AbstractQuestion;

@Entity
public class QuestionPool {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	@OneToMany
	private Set<AbstractQuestion<?, ?>> questions;
	
	private int maxScoreOfConcreteTest;
	
	private int percentageToPass;

	public Set<AbstractQuestion<?, ?>> getQuestions() {
		return Collections.unmodifiableSet(questions);
	}

	public int getMaxScoreOfConcreteTest() {
		return maxScoreOfConcreteTest;
	}

	public void setMaxScoreOfConcreteTest(int maxScoreOfConcreteTest) {
		this.maxScoreOfConcreteTest = maxScoreOfConcreteTest;
	}

	public int getPercentageToPass() {
		return percentageToPass;
	}

	public void setPercentageToPass(int percentageToPass) {
		this.percentageToPass = percentageToPass;
	}

	public long getId() {
		return id;
	}
}

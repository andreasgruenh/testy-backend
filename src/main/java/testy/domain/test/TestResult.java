package testy.domain.test;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import testy.domain.Account;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
public class TestResult {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	@ManyToOne
	@JsonIgnoreProperties({"testResults"})
	private Account user;
	
	private int score;
	
	private Timestamp timestamp;
	
	@ManyToOne
	@JsonIgnoreProperties({"categories", "percentageToPass", "testResultsByAccount", "getResults", "results"})
	private QuestionPool questionPool;
	
	public TestResult(Account user, QuestionPool pool, int score, long time) {
		this.user = user;
		this.questionPool = pool;
		this.score = score;
		this.timestamp = new Timestamp(time);
		
		if (user == null) {
			throw new NullPointerException();
		}
		if (pool == null) {
			throw new NullPointerException();
		}
		
		user.addTestResult(this);
		pool.addTestResult(this);
	}
	
	@Deprecated
	public TestResult() {
		
	}

	public Account getUser() {
		return user;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public Timestamp getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Timestamp timestamp) {
		this.timestamp = timestamp;
	}

	public long getId() {
		return id;
	}

	public QuestionPool getQuestionPool() {
		return questionPool;
	}

	public void setQuestionPool(QuestionPool questionPool) {
		this.questionPool = questionPool;
	}
	
	
}

package testy.domain.test;

import java.sql.Timestamp;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import testy.domain.Account;
import testy.domain.Subject;
import testy.domain.question.AbstractAnswer;

@Entity
public class TestResult {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	@ManyToOne
	private Account user;
	
	private Timestamp timestamp;
	
	@ManyToOne
	private Subject subject;
	
	@ManyToOne
	private QuestionPool questionPool;
	
	@OneToMany
	private Set<AbstractAnswer<?>> checkedAnswers = new HashSet<AbstractAnswer<?>>();
	
	@OneToMany
	private Set<AbstractAnswer<?>> uncheckedAnswers = new HashSet<AbstractAnswer<?>>();

	public Account getUser() {
		return user;
	}

	public void setUser(Account user) {
		this.user = user;
	}

	public Timestamp getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Timestamp timestamp) {
		this.timestamp = timestamp;
	}

	public Set<AbstractAnswer<?>> getCheckedAnswers() {
		return Collections.unmodifiableSet(checkedAnswers);
	}

	public void setCheckedAnswers(Set<AbstractAnswer<?>> checkedAnswers) {
		this.checkedAnswers = checkedAnswers;
	}

	public Set<AbstractAnswer<?>> getUncheckedAnswers() {
		return Collections.unmodifiableSet(uncheckedAnswers);
	}

	public void setUncheckedAnswers(Set<AbstractAnswer<?>> uncheckedAnswers) {
		this.uncheckedAnswers = uncheckedAnswers;
	}

	public long getId() {
		return id;
	}

	public Subject getSubject() {
		return subject;
	}

	public void setSubject(Subject subject) {
		this.subject = subject;
		
	}

	public QuestionPool getQuestionPool() {
		return questionPool;
	}

	public void setQuestionPool(QuestionPool questionPool) {
		this.questionPool = questionPool;
	}
	
	
}

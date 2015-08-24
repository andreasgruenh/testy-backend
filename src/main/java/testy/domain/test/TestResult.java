package testy.domain.test;

import java.sql.Timestamp;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import testy.domain.Account;
import testy.domain.question.AbstractAnswer;

@Entity
public class TestResult {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	@ManyToOne
	private Account user;
	
	private Timestamp timestamp;
	
	@OneToMany
	private Set<AbstractAnswer<?>> checkedAnswers;
	
	@OneToMany
	private Set<AbstractAnswer<?>> uncheckedAnswers;
	
}

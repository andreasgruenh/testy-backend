package testy.domain;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import testy.domain.test.QuestionPool;

@Entity
public class Subject {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	private String name;
	
	public Subject(String name) {
		this.name = name;
	}
	
	public Subject() {

	}
	
	public long getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@OneToMany(mappedBy = "subject", cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<QuestionPool> questionPools = new HashSet<QuestionPool>();

	public Set<QuestionPool> getQuestionPools() {
		return Collections.unmodifiableSet(questionPools);
	}

	public void addQuestionPool(QuestionPool pool) {
		if(pool == null) {
			throw new NullPointerException();
		}
		questionPools.add(pool);
		if(pool.getSubject() == null) {
			pool.setSubject(this);
		}
	}
}

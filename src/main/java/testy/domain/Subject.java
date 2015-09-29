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
import testy.domain.util.Views;

import com.fasterxml.jackson.annotation.JsonView;

@Entity
public class Subject {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@JsonView(Views.NoCircleView.class)
	private long id;
	
	@JsonView(Views.NoCircleView.class)
	private String name;
	
	public Subject(String name) {
		this.name = name;
	}
	
	public Subject() {

	}
	
	@JsonView(Views.NoCircleView.class)
	public long getId() {
		return id;
	}
	
	@JsonView(Views.NoCircleView.class)
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

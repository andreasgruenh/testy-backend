package testy.domain.test;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import testy.domain.Subject;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class QuestionPool {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	private String name;
	
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	@JsonIgnore
	private Set<Category> categories = new HashSet<Category>();
	
	private int percentageToPass;
	
	@ManyToOne
	private Subject subject;

	public QuestionPool(String name) {
		this.name = name;
	}
	
	public QuestionPool() {
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getMaxScoreOfConcreteTest() {
		int result = 0;
		for(Category category: categories) {
			result += category.getMaxScore();
		}
		return result;
	}
	
	// Is needed so that it can be ignored on posted pools
	@Deprecated
	public void setMaxScoreOfConcreteTest(int maxScoreOfConcreteTest) {
		
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
	
	public void addCategory(Category category) {
		if(category == null) {
			throw new NullPointerException();
		}
		categories.add(category);
	}
	
	public void removeCategory(Category category) {
		if(category == null) {
			throw new NullPointerException();
		}
		categories.remove(category);
	}
	
	public Set<Category> getCategories() {
		return Collections.unmodifiableSet(categories);
	}

	public Subject getSubject() {
		return subject;
	}

	public void setSubject(Subject subject) {
		this.subject = subject;
		if(!subject.getQuestionPools().contains(this)) {
			subject.addQuestionPool(this);
		}
	}
}

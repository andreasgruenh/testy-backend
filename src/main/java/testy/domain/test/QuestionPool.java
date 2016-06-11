package testy.domain.test;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import testy.domain.Account;
import testy.domain.Subject;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
public class QuestionPool {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	private String name;
	private String description;
	private String documentationFilePath;
	
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	@JsonIgnoreProperties({"pool"})
	private Set<Category> categories = new HashSet<Category>();
	
	@ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	private Map<Account, TestResult> testResultsByAccount = new HashMap<Account, TestResult>();
	
	private int percentageToPass;
	private int weeksAfterWhichTestHasToBeRepeated;
	
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDocumentationFilePath() {
		return documentationFilePath;
	}

	public void setDocumentationFilePath(String documentationFilePath) {
		this.documentationFilePath = documentationFilePath;
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

	public int getWeeksAfterWhichTestHasToBeRepeated() {
		return weeksAfterWhichTestHasToBeRepeated;
	}

	public void setWeeksAfterWhichTestHasToBeRepeated(int weeksAfterWhichTestHasToBeRepeated) {
		this.weeksAfterWhichTestHasToBeRepeated = weeksAfterWhichTestHasToBeRepeated;
	}

	public long getId() {
		return id;
	}
	
	public void addCategory(Category category) {
		if(category == null) {
			throw new NullPointerException();
		}
		categories.add(category);
		if(category.getPool() != this) {
			category.setPool(this);
		}
	}
	
	public void removeCategory(Category category) {
		if(category == null) {
			throw new NullPointerException();
		}
		categories.remove(category);
		if(category.getPool() != null) {
			category.setPool(null);
		}
	}
	
	public void addTestResult(TestResult result) {
		if (result == null) {
			throw new NullPointerException();
		}
		if (result.getUser() == null) {
			throw new NullPointerException();
		}
		this.testResultsByAccount.put(result.getUser(), result);
	}
	
	public void removeTestResult(TestResult result) {
		this.testResultsByAccount.remove(result.getUser());
		if (result.getUser().getTestResults().contains(result)) {
			result.getUser().removeTestResult(result);
		}
	}
	
	public Set<Category> getCategories() {
		return Collections.unmodifiableSet(categories);
	}
	
	public Collection<TestResult> getResults() {
		return Collections.unmodifiableCollection(testResultsByAccount.values());
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

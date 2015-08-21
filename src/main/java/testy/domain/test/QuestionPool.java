package testy.domain.test;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import testy.domain.question.Category;

@Entity
public class QuestionPool {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	@OneToMany
	private Set<Category> categories;
	
	private int percentageToPass;

	public int getMaxScoreOfConcreteTest() {
		int result = 0;
		for(Category category: categories) {
			result += category.getMaxScore();
		}
		return result;
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
	
	public Set<Category> getCategories() {
		return categories;
	}
}

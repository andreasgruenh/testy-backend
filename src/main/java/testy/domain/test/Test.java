package testy.domain.test;

import java.util.Set;

public class Test {

	private Set<Category> categories;
	
	public Test() {
		
	}
	
	public Test(Set<Category> categories) {
		this.categories = categories;
	}
	
	public Iterable<Category> getCategories() {
		return categories;
	}
	
	public void setCategories(Set<Category> categories) {
		this.categories = categories;
	}
	
}

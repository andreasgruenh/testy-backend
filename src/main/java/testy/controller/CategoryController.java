package testy.controller;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import testy.dataaccess.CategoryRepository;
import testy.domain.question.AbstractQuestion;
import testy.domain.test.Category;
import testy.domain.util.Views.Summary;

import com.fasterxml.jackson.annotation.JsonView;

@RestController
@RequestMapping("/categories")
public class CategoryController extends ApiController {
	
	@Autowired
	CategoryRepository catRepo;
	
	@JsonView(Summary.class)
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public Category getCategory(@PathVariable("id") long id) {
		return catRepo.findById(id);
	}
	
	@NeedsLoggedInAccount(admin = "true")
	@RequestMapping(value = "/{id}/questions", method = RequestMethod.GET)
	public Set<AbstractQuestion> updateCategory(@PathVariable("id") long id) {
		Category category = catRepo.findById(id);
		return category.getQuestions();
	}
	
	@NeedsLoggedInAccount(admin = "true")
	@RequestMapping(value = "/{id}", method = RequestMethod.PATCH)
	public Category updateCategory(@PathVariable("id") long id, @RequestBody Category postedCategory) {
		Category changedCategory = catRepo.findById(id);
		changedCategory.setMaxScore(postedCategory.getMaxScore());
		changedCategory.setName(postedCategory.getName());
		changedCategory = catRepo.save(changedCategory);
		return changedCategory;
	}
}

package testy.controller;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import testy.dataaccess.CategoryRepository;
import testy.dataaccess.QuestionRepository;
import testy.domain.question.AbstractQuestion;
import testy.domain.test.Category;
import testy.domain.util.Views.Summary;

import com.fasterxml.jackson.annotation.JsonView;

@RestController
@RequestMapping("/categories")
public class CategoryController extends ApiController {
	
	@Autowired
	CategoryRepository catRepo;
	
	@Autowired
	QuestionRepository questionRepo;
	
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
	
	@NeedsLoggedInAccount(admin = "true")
	@ResponseStatus(HttpStatus.CREATED)
	@RequestMapping(value = "/{id}/questions", method = RequestMethod.POST)
	public AbstractQuestion addNewQuestion(@PathVariable("id") long id, @RequestBody AbstractQuestion postedQuestion) {
		
		Category category = catRepo.findById(id);
		category.addQuestion(postedQuestion);
		catRepo.save(category);
		
		questionRepo.save(postedQuestion);
		return postedQuestion;	
	}
}

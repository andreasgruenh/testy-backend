package testy.controller;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import testy.dataaccess.AccountRepository;
import testy.dataaccess.CategoryRepository;
import testy.dataaccess.QuestionPoolRepository;
import testy.dataaccess.SubjectRepository;
import testy.domain.test.Category;
import testy.domain.test.QuestionPool;

@RestController
@RequestMapping("/pools")
public class QuestionPoolController extends ApiController {
	
	@Autowired
	AccountRepository accountRepo;
	
	@Autowired
	SubjectRepository subjectRepo;
	
	@Autowired
	QuestionPoolRepository questionPoolRepo;
	
	@Autowired
	CategoryRepository catRepo;
	
	@NeedsLoggedInAccount(admin = "true")
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public QuestionPool getQuestionPool(@PathVariable("id") long id) {
		return questionPoolRepo.findById(id);
	}
	
	@NeedsLoggedInAccount(admin = "true")
	@RequestMapping(value = "/{id}/categories", method = RequestMethod.GET)
	public Collection<Category> getAllCategories(@PathVariable("id") long id) {
		return questionPoolRepo.findById(id).getCategories();
	}
	
	@NeedsLoggedInAccount(admin = "true")
	@ResponseStatus(HttpStatus.CREATED)
	@RequestMapping(value = "/{id}/categories", method = RequestMethod.POST)
	public Category createNewCategory(@PathVariable("id") long id, @RequestBody Category postedCategory) {
		
		Category newCategory = new Category();
		newCategory.setName(postedCategory.getName());
		newCategory.setMaxScore(postedCategory.getMaxScore());
		QuestionPool pool = questionPoolRepo.findById(id);
		
		newCategory = catRepo.save(newCategory);
		pool.addCategory(newCategory);
		questionPoolRepo.save(pool);
		return newCategory;	
	}
}

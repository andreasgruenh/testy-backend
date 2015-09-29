package testy.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import testy.controller.exception.NotEnoughPermissionsException;
import testy.dataaccess.AccountRepository;
import testy.dataaccess.CategoryRepository;
import testy.dataaccess.QuestionPoolRepository;
import testy.dataaccess.SubjectRepository;
import testy.domain.question.Category;
import testy.domain.test.QuestionPool;
import testy.domain.util.Views;
import testy.security.service.CurrentAccountService;

import com.fasterxml.jackson.annotation.JsonView;

@RestController
@RequestMapping("/pools")
public class QuestionPoolController {

	@Autowired
	CurrentAccountService accountService;
	
	@Autowired
	AccountRepository accountRepo;
	
	@Autowired
	SubjectRepository subjectRepo;
	
	@Autowired
	QuestionPoolRepository questionPoolRepo;
	
	@Autowired
	CategoryRepository catRepo;
	
	@JsonView(Views.Summary.class)
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public QuestionPool getQuestionPool(@PathVariable("id") long id) {
		if(!accountService.getLoggedInAccount().isAdmin()) {
			throw new NotEnoughPermissionsException("Only admins may see QuestionPoolDetails");
		}
		return questionPoolRepo.findById(id);
	}
	
	@ResponseStatus(HttpStatus.CREATED)
	@RequestMapping(value = "/{id}/categories", method = RequestMethod.POST)
	public Category createNewCategory(@PathVariable("id") long id, @RequestBody Category postedCategory) {
		if(!accountService.getLoggedInAccount().isAdmin()) {
			throw new NotEnoughPermissionsException("Only admins may add new categories");
		}
		
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

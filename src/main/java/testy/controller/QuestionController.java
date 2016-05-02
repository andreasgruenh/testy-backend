package testy.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import testy.dataaccess.CategoryRepository;
import testy.dataaccess.QuestionRepository;
import testy.domain.question.AbstractQuestion;
import testy.domain.test.Category;

@RestController
@RequestMapping("/questions")
public class QuestionController extends ApiController {
	
	@Autowired
	CategoryRepository catRepo;
	
	@Autowired
	QuestionRepository questionRepo;
	
	@NeedsLoggedInAccount(admin = "true")
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public AbstractQuestion getQuestion(@PathVariable("id") long id) {
		return questionRepo.findById(id);
	}
	
	
	@NeedsLoggedInAccount(admin = "true")
	@RequestMapping(value = "/{id}", method = RequestMethod.PATCH)
	public AbstractQuestion updateQuestion(@PathVariable("id") long id, @RequestBody AbstractQuestion question) {
		AbstractQuestion oldQuestion = questionRepo.findById(id);
		Category category = oldQuestion.getCategory();
		oldQuestion.unsetCategory();
		questionRepo.delete(oldQuestion);
		question.setCategory(category);
		questionRepo.save(question);
		return question;
	}
	
	@NeedsLoggedInAccount(admin = "true")
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public void deleteQuestion(@PathVariable("id") long id) {
		AbstractQuestion question = questionRepo.findById(id);
		question.unsetCategory();
		questionRepo.delete(question);
	}
}

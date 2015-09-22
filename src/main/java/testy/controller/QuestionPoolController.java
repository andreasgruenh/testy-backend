package testy.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import testy.controller.exception.NotEnoughPermissionsException;
import testy.dataaccess.AccountRepository;
import testy.dataaccess.QuestionPoolRepository;
import testy.dataaccess.SubjectRepository;
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
	
	@JsonView(Views.Summary.class)
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public QuestionPool getQuestionPool(@PathVariable("id") long id) {
		if(!accountService.getLoggedInAccount().isAdmin()) {
			throw new NotEnoughPermissionsException("Only admins may see QuestionPoolDetails");
		}
		return questionPoolRepo.findById(id);
	}
}

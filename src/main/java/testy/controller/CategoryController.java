package testy.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;

import testy.controller.exception.NotEnoughPermissionsException;
import testy.dataaccess.CategoryRepository;
import testy.domain.test.Category;
import testy.domain.util.Views;
import testy.security.service.CurrentAccountService;

@RestController
@RequestMapping("/categories")
public class CategoryController {

	@Autowired
	CurrentAccountService accountService;
	
	@Autowired
	CategoryRepository catRepo;
	
	@JsonView(Views.Summary.class)
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public Category updateCategory(@PathVariable("id") long id, @RequestBody Category postedCategory) {
		if(!accountService.getLoggedInAccount().isAdmin()) {
			throw new NotEnoughPermissionsException("Only admins may update categories");
		}
		Category changedCategory = catRepo.findById(id);
		changedCategory.setMaxScore(postedCategory.getMaxScore());
		changedCategory.setName(postedCategory.getName());
		changedCategory = catRepo.save(changedCategory);
		return changedCategory;
	}
}

package testy.controller;

import java.io.File;
import java.io.IOException;
import java.net.SocketException;
import java.nio.file.Files;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;

import org.apache.catalina.connector.ClientAbortException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import testy.controller.exception.ResourceNotFoundException;
import testy.dataaccess.AccountRepository;
import testy.dataaccess.CategoryRepository;
import testy.dataaccess.QuestionPoolRepository;
import testy.dataaccess.SubjectRepository;
import testy.dataaccess.TestResultRepository;
import testy.domain.question.AbstractAnswer;
import testy.domain.test.Category;
import testy.domain.test.QuestionPicker;
import testy.domain.test.QuestionPool;
import testy.domain.test.TestResult;
import testy.domain.test.TestResultFactory;
import testy.domain.test.TestValidator;
import testy.domain.util.Views.Summary;

import com.fasterxml.jackson.annotation.JsonView;

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
	TestResultRepository testResultRepo;
	
	@Autowired
	QuestionPicker picker;
	
	@Autowired
	TestValidator validator;
	
	@Autowired
	Environment env;
	
	@Autowired
	CategoryRepository catRepo;
	
	@Autowired
	TestResultFactory testResultFactory;
	
	@NeedsLoggedInAccount(admin = "true")
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public QuestionPool getQuestionPool(@PathVariable("id") long id) {
		return questionPoolRepo.findById(id);
	}
	
	@NeedsLoggedInAccount(admin = "true")
	@RequestMapping(value = "/{id}", method = RequestMethod.PATCH)
	public QuestionPool updateQuestionPool(@PathVariable("id") long id, @RequestBody QuestionPool changedPool) {
		QuestionPool oldPool = questionPoolRepo.findById(id);
		oldPool.setDescription(changedPool.getDescription());
		oldPool.setName(changedPool.getName());
		oldPool.setPercentageToPass(changedPool.getPercentageToPass());
		oldPool.setWeeksAfterWhichTestHasToBeRepeated(changedPool.getWeeksAfterWhichTestHasToBeRepeated());
		questionPoolRepo.save(oldPool);
		return oldPool;
	}
	
	@NeedsLoggedInAccount(admin = "true")
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public void deleteQuestionPool(@PathVariable("id") long id) {
		QuestionPool pool = questionPoolRepo.findById(id);
		pool.getSubject().removeQuestionPool(pool);
		questionPoolRepo.delete(id);
	}
	
	@NeedsLoggedInAccount(admin = "true")
	@JsonView(Summary.class)
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
	
	@JsonView(Summary.class)
	@NeedsLoggedInAccount
	@RequestMapping(value = "/{id}/test", method = RequestMethod.GET)
	public Collection<Category> getTest(@PathVariable("id") long id) {
		return picker.generateCategoriesWithRandomQuestionsFrom(questionPoolRepo.findById(id));
	}
	
	@NeedsLoggedInAccount
	@RequestMapping(value = "/{id}/test", method = RequestMethod.POST)
	public TestResult getTestResult(@PathVariable("id") long id, @SuppressWarnings("rawtypes") @RequestBody AbstractAnswer[] answers) {
		int absoluteScore = validator.validateTest(answers);
		QuestionPool pool = questionPoolRepo.findById(id);
		int score = (absoluteScore * 100 ) / pool.getMaxScoreOfConcreteTest();
		if (pool.getPercentageToPass() > score) {
			return testResultFactory.createTestResult(this.loggedInAccount, pool, score);			
		}
		TestResult result = 
			testResultFactory.createTestResult(this.loggedInAccount, pool, score);
		testResultRepo.save(result);
		questionPoolRepo.save(pool);
		accountRepo.save(loggedInAccount);
		return result;
	}
	
	@NeedsLoggedInAccount(admin = "true")
	@RequestMapping(value ="/{id}/material", method = RequestMethod.POST)
	public void uploadFile(@PathVariable("id") long id, @RequestParam MultipartFile file) throws IllegalStateException, IOException {
		String path = env.getProperty("uploads.path") + file.getOriginalFilename();
		QuestionPool pool = questionPoolRepo.findById(id);
		try {
			Files.delete(new File(pool.getDocumentationFilePath()).toPath());
		} catch (Exception e) {
			
		}
		file.transferTo(new File(path));
		pool.setDocumentationFilePath(path);
		questionPoolRepo.save(pool);
	}
	
	@NeedsLoggedInAccount
	@RequestMapping(value ="/{id}/material", method = RequestMethod.GET, produces = "application/pdf")
	public FileSystemResource uploadFile(@PathVariable("id") long id) {
		QuestionPool pool = questionPoolRepo.findById(id);
		if (pool == null) {
			throw new ResourceNotFoundException("There is no question pool with this id");
		}
		try {
			File file = new File(pool.getDocumentationFilePath());
			return new FileSystemResource(file);
		} catch(Exception e) {
			throw new ResourceNotFoundException("This question pool has no file");
		}
	}
	
	@ResponseStatus(value=HttpStatus.OK)
	@ExceptionHandler({SocketException.class, ClientAbortException.class})
	public void handleChromeBug(HttpServletRequest req, Exception exception) {

	}
}

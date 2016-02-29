package testy.helper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import testy.dataaccess.AccountRepository;
import testy.dataaccess.CategoryRepository;
import testy.dataaccess.QuestionPoolRepository;
import testy.dataaccess.QuestionRepository;
import testy.dataaccess.SubjectRepository;
import testy.domain.Account;
import testy.domain.Subject;
import testy.domain.question.mc.MCPossibility;
import testy.domain.question.mc.MCQuestion;
import testy.domain.test.Category;
import testy.domain.test.QuestionPool;
import testy.domain.util.AccountBuilder;

@Service
public class TestClasses {

	@Autowired
	Environment env;
	
	@Autowired
	AccountRepository accountRepo;
	@Autowired
	SubjectRepository subjectRepo;
	@Autowired
	QuestionPoolRepository questionPoolRepo;
	@Autowired
	CategoryRepository categoryRepo;
	@Autowired
	QuestionRepository questionRepo;
	
	public Account user;
	public String userPassword;
	
	public Account admin;
	public String adminPassword;
	
	public Subject subject1;
	public QuestionPool questionPool1;
	public Category category1;
	public MCQuestion question1;
	public MCQuestion question2;
	public MCPossibility possibility1;
	public MCPossibility possibility2;
	public MCPossibility possibility3;
	public MCPossibility possibility4;
	
	public void init() {
		user = AccountBuilder.startWith(env.getProperty("ldap.loginTester"))
				.isAdmin(false)
				.withEmail("andreas.roth@paul-consultants.de")
				.withFirstname("Andreas")
				.withLastname("Roth")
				.build();
		userPassword = env.getProperty("ldap.loginTesterPw");
		
		admin = AccountBuilder.startWith(env.getProperty("ldap.loginAdmin"))
				.isAdmin(true)
				.withEmail("admin@paul-consultants.de")
				.withFirstname("Admin")
				.withLastname("Admin")
				.build();
		adminPassword = env.getProperty("ldap.loginAdminPw");
		
		subject1 = new Subject("Fach 1");
		subject1.setDescription("This is a new subject");
		questionPool1 = new QuestionPool("Pool 1");
		questionPool1.setPercentageToPass(80);
		questionPool1.setDescription("This is a new pool");
		questionPool1.setSubject(subject1);
		category1 = new Category("Kategorie 1");
		category1.setMaxScore(20);
		question1 = new MCQuestion();
		question1.setCategory(category1);
		question1.setQuestionString("Wieso?");
		possibility1 = new MCPossibility("A1", true);
		possibility2 = new MCPossibility("A2", false);
		question1.addAnswer(possibility1);
		question1.addAnswer(possibility2);
		question2 = new MCQuestion();
		question2.setCategory(category1);
		question2.setQuestionString("Warum?");
		possibility3 = new MCPossibility("B1", true);
		possibility4 = new MCPossibility("B2", false);
		question2.addAnswer(possibility3);
		question2.addAnswer(possibility4);
		questionPool1.addCategory(category1);
		subject1.addQuestionPool(questionPool1);
		
	}
	
	public void initWithDb() {
		init();
		user = accountRepo.save(user);
		admin = accountRepo.save(admin);
		subject1 = subjectRepo.save(subject1);
		questionPool1 = questionPoolRepo.save(questionPool1);
		category1 = categoryRepo.save(category1);
		question1 = questionRepo.save(question1);
		question2 = questionRepo.save(question2);
		
	}
}

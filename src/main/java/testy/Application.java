package testy;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import testy.dataaccess.AccountRepository;
import testy.dataaccess.QuestionRepository;
import testy.domain.Account;
import testy.domain.question.image.ImageQuestion;
import testy.domain.question.mc.MCQuestion;



/**
 * Acts as a servlet initializer and start class.
 * 
 * @author andi
 */
@Configuration
@ComponentScan
@EnableAutoConfiguration
public class Application extends SpringBootServletInitializer implements CommandLineRunner {

	@Autowired
	private AccountRepository accountRepo;
	
	@Autowired
	private Environment env;
	
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Override
	public void run(String... strings) throws Exception {
		if (env.getProperty("spring.profiles.active").equals("dev")) {
			accountRepo.save(new Account("tschulz"));
			accountRepo.save(new Account("tkalwa"));
			accountRepo.save(new Account("afriedemann"));
		}
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(Application.class);
	}
}

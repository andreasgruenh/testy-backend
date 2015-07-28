package testy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;



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
	private Environment environment;
	
	
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Override
	public void run(String... strings) throws Exception {
		String[] envVars = environment.getActiveProfiles();
		if (!(envVars.length > 0 && envVars[0].equals("demo"))) {
			
		}
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(Application.class);
	}
}

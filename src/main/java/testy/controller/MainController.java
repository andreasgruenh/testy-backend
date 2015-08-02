package testy.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import testy.domain.Account;
import testy.security.service.CurrentAccountService;

@RestController
@RequestMapping("/")
public class MainController {

	@Autowired
	CurrentAccountService accountService;
	
	@RequestMapping(value = "", method = RequestMethod.GET)
	String hello() {
		return "Hello World!";
	}
	
	@RequestMapping(value = "/me", method = RequestMethod.GET)
	Account getCurrentUser() {
		return accountService.getLoggedInAccount();
	}

	@Autowired
	private Environment environment;

	@RequestMapping("/profile")
	String getProfile() {
		if(environment.getActiveProfiles().length > 0) {
			return environment.getActiveProfiles()[0];
		} else {
			return "";
		}
	}

}

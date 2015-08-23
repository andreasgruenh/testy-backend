package testy.controller;


import java.util.Set;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import testy.domain.question.AbstractQuestion;

@RestController
@RequestMapping("/test")
public class DeleteMeController {

	@RequestMapping(value = "/", method = RequestMethod.POST)
	AbstractQuestion<?, ?> updateAccount(@RequestBody Set<AbstractQuestion<?, ?>> questions) {
		int counter = 0;
		AbstractQuestion<?,?> result = null;	
		for(AbstractQuestion<?, ?> question: questions){
			System.out.println(question.getClass());
			if(counter == 0) result = question;
			counter++;
		}
		return result;

		
		
	}
	
}

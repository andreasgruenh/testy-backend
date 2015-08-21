package testy.domain.question.mc;

import testy.domain.question.AbstractQuestionValidator;

public class MCQuestionValidator extends AbstractQuestionValidator<MCQuestion, MCAnswer> {

	@Override
    public int validate(MCQuestion question, MCAnswer answer) {
	    return 0;
    }
	
}

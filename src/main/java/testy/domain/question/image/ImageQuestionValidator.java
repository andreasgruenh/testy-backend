package testy.domain.question.image;

import testy.domain.question.AbstractQuestionValidator;

public class ImageQuestionValidator extends AbstractQuestionValidator<ImageQuestion, ImageAnswer> {

	@Override
    public int validate(ImageQuestion question, ImageAnswer answer) {
	    return 0;
    }

}

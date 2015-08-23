package testy.domain.question.image;

import javax.persistence.Entity;

import testy.domain.question.AbstractQuestion;
import testy.domain.question.QuestionType;

@Entity
public class ImageQuestion extends AbstractQuestion<ImageQuestion, ImageAnswer>{

	public ImageQuestion() {
		type = QuestionType.ImageQuestion;
	}
	
}

package testy.domain.question.image;

import javax.persistence.Entity;

import testy.domain.question.AbstractQuestion;

import com.fasterxml.jackson.annotation.JsonTypeName;

@Entity
@JsonTypeName("ImageQuestion")
public class ImageQuestion extends AbstractQuestion<ImageQuestion, ImageAnswer>{

	public ImageQuestion() {
	}
	
}

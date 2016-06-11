package testy.domain.question.image;

import javax.persistence.Entity;

import testy.domain.question.AbstractAnswer;

import com.fasterxml.jackson.annotation.JsonTypeName;

@Entity
@JsonTypeName("ImageAnswer")
public class ImageAnswer extends AbstractAnswer<ImageQuestion> {

	@Override
    public int validate() {
	    // TODO Auto-generated method stub
	    return 0;
    }

}

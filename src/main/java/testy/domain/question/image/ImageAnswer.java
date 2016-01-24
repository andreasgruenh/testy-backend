package testy.domain.question.image;

import javax.persistence.Entity;

import com.fasterxml.jackson.annotation.JsonTypeName;

import testy.domain.question.AbstractAnswer;

@Entity
@JsonTypeName("ImageAnswer")
public class ImageAnswer extends AbstractAnswer<ImageQuestion> {

	@Override
    public int validate() {
	    // TODO Auto-generated method stub
	    return 0;
    }

}

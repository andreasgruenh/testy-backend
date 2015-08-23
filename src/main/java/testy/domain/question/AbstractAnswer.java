package testy.domain.question;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public abstract class AbstractAnswer {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	protected long id;
	
	public abstract int validate();
	
}

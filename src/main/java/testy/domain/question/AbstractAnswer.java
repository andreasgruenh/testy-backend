package testy.domain.question;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
public abstract class AbstractAnswer<Q extends AbstractQuestion> {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	protected long id;
	
	@OneToOne
	protected Q question;
	
	public abstract int validate();
}

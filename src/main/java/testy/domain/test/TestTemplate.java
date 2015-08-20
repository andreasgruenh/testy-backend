package testy.domain.test;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class TestTemplate {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	
	
}

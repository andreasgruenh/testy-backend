package testy.dataaccess;

import org.springframework.data.repository.CrudRepository;

import testy.domain.question.AbstractQuestion;



public interface QuestionRepository extends CrudRepository<AbstractQuestion, Long> {
	
	Iterable<AbstractQuestion> findAll();
	
}

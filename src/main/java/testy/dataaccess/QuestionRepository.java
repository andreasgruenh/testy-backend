package testy.dataaccess;

import java.util.Collection;

import org.springframework.data.repository.CrudRepository;

import testy.domain.question.AbstractQuestion;

public interface QuestionRepository extends CrudRepository<AbstractQuestion, Long> {

	Collection<AbstractQuestion> findAll();

}

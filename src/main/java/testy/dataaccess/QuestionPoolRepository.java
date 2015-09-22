package testy.dataaccess;

import org.springframework.data.repository.CrudRepository;

import testy.domain.test.QuestionPool;



public interface QuestionPoolRepository extends CrudRepository<QuestionPool, Long> {
	
	QuestionPool findById(long id);
	
}

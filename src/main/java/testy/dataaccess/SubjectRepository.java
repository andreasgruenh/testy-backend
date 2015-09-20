package testy.dataaccess;

import java.util.Collection;

import org.springframework.data.repository.CrudRepository;

import testy.domain.Subject;



public interface SubjectRepository extends CrudRepository<Subject, Long> {
	
	Collection<Subject> findAll();
	
	Subject findById(long id);
	
}

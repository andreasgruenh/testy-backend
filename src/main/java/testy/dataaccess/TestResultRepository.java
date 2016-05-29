package testy.dataaccess;

import java.util.Collection;

import org.springframework.data.repository.CrudRepository;

import testy.domain.test.TestResult;



public interface TestResultRepository extends CrudRepository<TestResult, Long> {
	
	Collection<TestResult> findAll();
	
	TestResult findById(long id);
	
}

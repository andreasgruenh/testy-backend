package testy.dataaccess;

import java.util.Collection;

import org.springframework.data.repository.CrudRepository;

import testy.domain.test.Category;



public interface CategoryRepository extends CrudRepository<Category, Long> {
	
	Collection<Category> findAll();
	
	Category findById(long id);
	
}

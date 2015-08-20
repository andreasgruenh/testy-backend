package testy.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import testy.domain.test.TestTemplate;
import testy.domain.util.Views;

import com.fasterxml.jackson.annotation.JsonView;

@Entity
public class Subject {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@JsonView(Views.Summary.class)
	private long id;
	
	@JsonView(Views.Summary.class)
	private String name;
	
	private TestTemplate test;
	
}

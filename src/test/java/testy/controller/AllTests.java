package testy.controller;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ AccountControllerTest.class, CategoryControllerTest.class, ControllerTest.class,
		LdapLoginTest.class, MainControllerTest.class, QuestionControllerTest.class,
		QuestionPoolControllerTest.class, SubjectControllerTest.class, TestResultControllerTest.class })
public class AllTests {

}

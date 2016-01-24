package testy.controller;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import testy.Application;
import testy.helper.SessionEstablisher;
import testy.helper.TestClasses;
import testy.helper.annotations.AnnotationChecker;

import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@Transactional
@IntegrationTest
public class ControllerTest {

	protected MockMvc mockMvc;
	
	@Autowired
	private FilterChainProxy filterChainProxy;

	@Autowired
	private WebApplicationContext webAppContext;
	
	@Autowired
	protected TestClasses testClasses;
	
	protected ObjectMapper mapper;
	
	@Autowired
	private SessionEstablisher sessionEstablisher;
	
	protected MockHttpSession userSession;
	protected MockHttpSession adminSession;
	
	@Rule
    public AnnotationChecker annotationChecker = new AnnotationChecker();
	
	@Before
	public void prepareTest() throws Exception {
		
		mapper = new ObjectMapper();
		
		mockMvc = MockMvcBuilders.webAppContextSetup(webAppContext).dispatchOptions(true)
		        .addFilters(filterChainProxy).build();

		if(annotationChecker.needsTestClasses) {
			testClasses.initWithDb();
		}
		
		if(annotationChecker.needsSessions) {
			userSession = sessionEstablisher.getUserSessionWith(mockMvc);
			adminSession = sessionEstablisher.getAdminSessionWith(mockMvc);
		}
		
	}
	
	@Test
	public void dummyTest() {
		assertTrue(true);
	}
	
}

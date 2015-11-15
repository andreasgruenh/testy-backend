package testy.controller;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import testy.Application;
import testy.dataaccess.QuestionPoolRepository;
import testy.dataaccess.SubjectRepository;
import testy.domain.test.Category;
import testy.helper.SessionEstablisher;
import testy.helper.TestClasses;

import com.fasterxml.jackson.databind.ObjectMapper;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.iterableWithSize;
import static org.hamcrest.Matchers.nullValue;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@Transactional
@IntegrationTest
public class CategoryControllerTest {

	private MockMvc mockMvc;

	@Autowired
	private FilterChainProxy filterChainProxy;

	@Autowired
	private WebApplicationContext webAppContext;

	@Autowired
	private TestClasses testClasses;
	
	@Autowired
	private SessionEstablisher sessionEstablisher;

	private MockHttpSession userSession;
	private MockHttpSession adminSession;

	@Autowired
	private SubjectRepository subjectRepo;

	@Autowired
	private QuestionPoolRepository poolRepo;

	ObjectMapper mapper;

	@Before
	public void setUp() throws Exception {

		testClasses.initWithDb();
		mapper = new ObjectMapper();

		mockMvc = MockMvcBuilders.webAppContextSetup(webAppContext).dispatchOptions(true)
		        .addFilters(filterChainProxy).build();

		userSession = sessionEstablisher.getUserSessionWith(mockMvc);
		adminSession = sessionEstablisher.getAdminSessionWith(mockMvc);
	}

	@Test
	public void GET_id_shouldReturnCorrectCategoryWithCorrectProperties() throws Exception {
		// act
		mockMvc.perform(get("/categories/" + testClasses.category1.getId()).session(userSession)).andExpect(status().isOk())
		
		// assert
		.andExpect(jsonPath("$.id", is((int) testClasses.category1.getId())))
		.andExpect(jsonPath("$.name", is(testClasses.category1.getName())))
		.andExpect(jsonPath("$.maxScore", is(testClasses.category1.getMaxScore())))
		.andExpect(jsonPath("$.questions", nullValue()));
	}
	
	@Test
	public void GET_id_questions_withoutAdminPermissions_shouldReturn403() throws Exception {
		// act + assert
		mockMvc.perform(get("/categories/" + testClasses.category1.getId() + "/questions").session(userSession)).andExpect(status().isForbidden());		
	}
	
	@Test
	public void GET_id_questions_withAdminPermissions_shouldReturnQuestions() throws Exception {
		// act
		mockMvc.perform(get("/categories/" + testClasses.category1.getId() + "/questions").session(adminSession)).andExpect(status().isOk())
		
		// assert
		.andExpect(jsonPath("$", iterableWithSize(testClasses.category1.getQuestions().size())));
	}
	
	@Test
	public void PUT_id_withoutAdminPermissions_shouldReturn403() throws Exception {
		// arrange
		Category changedCat = new Category("Changed Category");
		changedCat.setMaxScore(40);

		// act
		mockMvc.perform(
		        put("/categories/" + testClasses.category1.getId()).session(userSession)
		                .contentType(MediaType.APPLICATION_JSON)
		                .content(mapper.writeValueAsString(changedCat))).andExpect(
		        status().isForbidden());
	}

	@Test
	public void PUT_id_withAdminPermissions_shouldReturnChangedCategory() throws Exception {
		// arrange
		Category changedCat = new Category("Changed Category");
		changedCat.setMaxScore(40);

		// act
		mockMvc.perform(
		        put("/categories/" + testClasses.category1.getId()).session(adminSession)
		                .contentType(MediaType.APPLICATION_JSON)
		                .content(mapper.writeValueAsString(changedCat))).andExpect(status().isOk())

		// assert
		        .andExpect(jsonPath("$.name", is(equalTo(changedCat.getName()))))
		        .andExpect(jsonPath("$.maxScore", is(equalTo(changedCat.getMaxScore()))));
	}
}

package testy.controller;

import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import testy.Application;
import testy.dataaccess.SubjectRepository;
import testy.domain.Subject;
import testy.domain.test.QuestionPool;

import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@Transactional
@IntegrationTest
public class SubjectControllerTest {

	private MockMvc mockMvc;

	@Autowired
	private FilterChainProxy filterChainProxy;

	@Autowired
	private WebApplicationContext webAppContext;

	@Autowired
	private Environment env;

	private MockHttpSession userSession;
	private MockHttpSession adminSession;

	@Autowired
	private SubjectRepository subjectRepo;

	private Subject subject1;
	long subject1Id;

	ObjectMapper mapper = new ObjectMapper();

	@Before
	public void setUp() throws Exception {

		mockMvc = MockMvcBuilders.webAppContextSetup(webAppContext).dispatchOptions(true)
		        .addFilters(filterChainProxy).build();

		userSession = (MockHttpSession) mockMvc
		        .perform(
		                post("/login").param("username", env.getProperty("ldap.loginTester"))
		                        .param("password", env.getProperty("ldap.loginTesterPw")))
		        .andExpect(status().isOk()).andReturn().getRequest().getSession();

		adminSession = (MockHttpSession) mockMvc
		        .perform(
		                post("/login").param("username", env.getProperty("ldap.loginAdmin")).param(
		                        "password", env.getProperty("ldap.loginAdminPw")))
		        .andExpect(status().isOk()).andReturn().getRequest().getSession();

		subject1 = new Subject("Fach1");
		Subject subject2 = new Subject("Fach2");
		Subject subject3 = new Subject("Fach3");

		QuestionPool pool1 = new QuestionPool("pool1");
		QuestionPool pool2 = new QuestionPool("pool2");

		subjectRepo.save(Arrays.asList(subject1, subject2, subject3));

		subject1.addQuestionPool(pool1);
		subject1.addQuestionPool(pool2);
		subject1 = subjectRepo.save(subject1);
		subject1Id = subject1.getId();
	}

	@Test
	public void GET_subjects_shouldReturnTheCorrectAmmountOfSubjects() throws Exception {

		MockHttpServletResponse response = mockMvc.perform(get("/subjects/").session(userSession))
		        .andExpect(status().isOk()).andReturn().getResponse();

		Subject[] subjects = mapper.readValue(response.getContentAsString(), Subject[].class);

		assertTrue("Response should contain exactly 4 object", subjects.length == 3);
	}

	@Test
	public void POST_subjects_withoutAdminPermissions_shouldReturn403() throws Exception {

		Subject newSubject = new Subject("Fach4");

		mockMvc.perform(
		        post("/subjects/").session(userSession).contentType(MediaType.APPLICATION_JSON)
		                .content(mapper.writeValueAsString(newSubject))).andExpect(
		        status().isForbidden());
	}

	@Test
	public void POST_subjects_withPermissions_shouldCreateTheNewSubject() throws Exception {

		Subject newSubject = new Subject("Fach4");

		MockHttpServletResponse response = mockMvc
		        .perform(
		                post("/subjects/").session(adminSession)
		                        .contentType(MediaType.APPLICATION_JSON)
		                        .content(mapper.writeValueAsString(newSubject)))
		        .andExpect(status().isCreated()).andReturn().getResponse();

		Subject createdSubject = mapper.readValue(response.getContentAsString(), Subject.class);
		assertTrue("Name of new subject should be equal to posted name", createdSubject.getName()
		        .equals(newSubject.getName()));
	}

	@Test
	public void GET_subjectsIdPools_shouldReturnAllQuestionPools() throws Exception {

		MockHttpServletResponse response = mockMvc
		        .perform(get("/subjects/" + subject1Id + "/pools").session(userSession))
		        .andExpect(status().isOk()).andReturn().getResponse();
		
		QuestionPool[] pools = mapper
		        .readValue(response.getContentAsString(), QuestionPool[].class);
		assertTrue("Ammount of questionPools not correct", pools.length == 2);
	}

	@Test
	public void POST_subjectsIdPools_withWrongPermissions_shouldReturn403() throws Exception {

		mockMvc.perform(post("/subjects/" + subject1Id + "/pools").session(userSession).param(
		        "name", "new Pool"));
	}

	@Test
	public void POST_subjectsIdPools_withCorrectPermissions_shouldCreatePoolWithCorrectName()
	        throws Exception {

		QuestionPool newPool = new QuestionPool("newPool");

		MockHttpServletResponse response = mockMvc
		        .perform(
		                post("/subjects/" + subject1Id + "/pools").session(adminSession).param(
		                        "name", newPool.getName())).andExpect(status().isCreated()).andReturn()
		        .getResponse();

		QuestionPool createdPool = mapper.readValue(response.getContentAsString(),
		        QuestionPool.class);

		assertTrue("Name of created pool should equal posted pool, it is '" + createdPool.getName() + "' but expected was '" + newPool.getName() + "'",
		        newPool.getName().equals(createdPool.getName()));
		assertTrue("Subject should be set correctly",
		        createdPool.getSubject().getId() == subject1Id);
	}
}

package testy.controller;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
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
import testy.dataaccess.QuestionPoolRepository;
import testy.dataaccess.SubjectRepository;
import testy.domain.Subject;
import testy.domain.question.Category;
import testy.domain.test.QuestionPool;
import testy.helper.SessionEstablisher;

import com.fasterxml.jackson.databind.ObjectMapper;

import static org.junit.Assert.assertTrue;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@Transactional
@IntegrationTest
public class QuestionPoolControllerTest {

	private MockMvc mockMvc;

	@Autowired
	private FilterChainProxy filterChainProxy;

	@Autowired
	private WebApplicationContext webAppContext;

	@Autowired
	private SessionEstablisher sessionEstablisher;

	private MockHttpSession userSession;
	private MockHttpSession adminSession;

	@Autowired
	private SubjectRepository subjectRepo;

	@Autowired
	private QuestionPoolRepository poolRepo;

	private Subject subject1;
	long subject1Id;

	private QuestionPool pool1;
	private Category cat1;

	ObjectMapper mapper = new ObjectMapper();

	@Before
	public void setUp() throws Exception {

		mockMvc = MockMvcBuilders.webAppContextSetup(webAppContext).dispatchOptions(true)
		        .addFilters(filterChainProxy).build();

		userSession = sessionEstablisher.getUserSessionWith(mockMvc);

		adminSession = sessionEstablisher.getAdminSessionWith(mockMvc);

		subject1 = new Subject("Fach1");
		subjectRepo.save(subject1);

		pool1 = new QuestionPool("pool1");
		cat1 = new Category("cat1");
		cat1.setMaxScore(20);
		pool1.addCategory(cat1);
		pool1 = poolRepo.save(pool1);

		subject1.addQuestionPool(pool1);
		subject1 = subjectRepo.save(subject1);
	}

	@Test
	public void GET_poolsId_withoutPermissionsShouldReturn403() throws Exception {

		mockMvc.perform(get("/pools/" + pool1.getId()).session(userSession)).andExpect(
		        status().isForbidden());
	}

	@Test
	public void GET_poolsId_withPermissionsShouldReturnCorrectPool() throws Exception {

		MockHttpServletResponse response = mockMvc.perform(get("/pools/" + pool1.getId()).session(adminSession)).andExpect(
		        status().isOk()).andReturn().getResponse();
		
		QuestionPool returnedPool = mapper.readValue(response.getContentAsString(), QuestionPool.class);
		
		assertTrue("Name of pool should be returned", returnedPool.getName().equals(pool1.getName()));
		assertTrue("Id of pool should be returned", returnedPool.getId() == pool1.getId());
		assertTrue("Categories should be returned", returnedPool.getCategories().size() == 1);
	}
}

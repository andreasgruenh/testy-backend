package testy.controller;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
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
import testy.dataaccess.QuestionPoolRepository;
import testy.dataaccess.SubjectRepository;
import testy.domain.Subject;
import testy.domain.question.AbstractQuestion;
import testy.domain.question.Category;
import testy.domain.question.mc.MCQuestion;
import testy.domain.test.QuestionPool;
import testy.helper.SessionEstablisher;

import com.fasterxml.jackson.databind.ObjectMapper;

import static java.util.Arrays.asList;

import static org.junit.Assert.assertTrue;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

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

		AbstractQuestion quest1 = new MCQuestion();
		quest1.setQuestionString("Frage 1");
		AbstractQuestion quest2 = new MCQuestion();
		quest2.setQuestionString("Frage 2");
		cat1.addAllQuestions(asList(quest1, quest2));
		pool1.addCategory(cat1);
		pool1 = poolRepo.save(pool1);
		cat1 = pool1.getCategories().iterator().next();

		subject1.addQuestionPool(pool1);
		subject1 = subjectRepo.save(subject1);
	}

	@Test
	public void PUT_id_withoutAdminPermissions_shouldReturn403() throws Exception {

		// arrange
		Category changedCat = new Category("Changed Category");
		changedCat.setMaxScore(40);

		// act
		mockMvc.perform(
		        put("/categories/" + cat1.getId()).session(userSession)
		                .contentType(MediaType.APPLICATION_JSON)
		                .content(mapper.writeValueAsString(changedCat))).andExpect(
		        status().isForbidden());
	}

	@Test
	public void PUT_id_withAdminPermissions_shouldReturnChangedCategory()
	        throws Exception {

		// arrange
		Category changedCat = new Category("Changed Category");
		changedCat.setMaxScore(40);

		// act
		MockHttpServletResponse response = mockMvc
		        .perform(
		                put("/categories/" + cat1.getId())
		                        .session(adminSession).contentType(MediaType.APPLICATION_JSON)
		                        .content(mapper.writeValueAsString(changedCat)))
		        .andExpect(status().isOk()).andReturn().getResponse();

		// assert
		Category actualCategory = mapper.readValue(response.getContentAsString(), Category.class);
		assertTrue("Name of category should be returned",
		        actualCategory.getName().equals(changedCat.getName()));
		assertTrue("Max Score of category should be returned",
		        actualCategory.getMaxScore() == changedCat.getMaxScore());
	}

	@Test
	public void PUT_id_withAdminPermissions_shouldSaveNewCategory()
	        throws Exception {

		// arrange
		Category changedCat = new Category("Changed Category");
		changedCat.setMaxScore(40);

		// act
		mockMvc.perform(
		        put("/categories/" + cat1.getId())
		                .session(adminSession).contentType(MediaType.APPLICATION_JSON)
		                .content(mapper.writeValueAsString(changedCat))).andExpect(status().isOk())
		        .andReturn().getResponse();

		// assert
		MockHttpServletResponse response = mockMvc
		        .perform(get("/pools/" + pool1.getId()).session(adminSession))
		        .andExpect(status().isOk()).andReturn().getResponse();

		Category actualCat = mapper.readValue(response.getContentAsString(),
		        QuestionPool.class).getCategories().iterator().next();
		assertTrue("Category name should have been updated", actualCat.getName().equals(changedCat.getName()));
		assertTrue("Category maxScore should have been updated", actualCat.getMaxScore() == changedCat.getMaxScore());
	}
}

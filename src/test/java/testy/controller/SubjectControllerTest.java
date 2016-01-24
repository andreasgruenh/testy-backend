package testy.controller;

import static org.hamcrest.Matchers.any;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.ResultActions;

import testy.dataaccess.SubjectRepository;
import testy.domain.Subject;
import testy.domain.test.QuestionPool;
import testy.helper.JsonIterableChecker;
import testy.helper.annotations.NeedsSessions;
import testy.helper.annotations.NeedsTestClasses;

public class SubjectControllerTest extends ControllerTest {

	@Autowired
	private SubjectRepository subjectRepo;

	@NeedsSessions
	@NeedsTestClasses
	@Test
	public void GET_subjects_shouldReturnCollectionOfSubjects() throws Exception {

		// act
		String response = mockMvc.perform(get("/subjects/").session(userSession))
			.andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

		// assert
		assertTrue("Collection of Subjects should be returned, but was: " + response,
			JsonIterableChecker.representsIterableOfClass(response, Subject.class));
	}

	@NeedsSessions
	@NeedsTestClasses
	@Test
	public void GET_subjects_shouldReturnCorrectProperties() throws Exception {

		// act
		mockMvc.perform(get("/subjects/").session(userSession))
			.andExpect(status().isOk())

			// assert
			.andExpect(jsonPath("$[0].id", is(any(Integer.class))))
			.andExpect(jsonPath("$[0].name", is(any(String.class))))
			.andExpect(jsonPath("$[0].questionPools").doesNotExist());

	}

	@NeedsSessions
	@NeedsTestClasses
	@Test
	public void POST_subjects_withoutAdminPermissions_shouldReturn403() throws Exception {

		// arrange
		Subject newSubject = new Subject("Fach4");

		// act
		mockMvc.perform(post("/subjects/").session(userSession).contentType(MediaType.APPLICATION_JSON)
			.content(mapper.writeValueAsString(newSubject)))

			// assert
			.andExpect(status().isForbidden());
	}

	@NeedsSessions
	@NeedsTestClasses
	@Test
	public void POST_subjects_withPermissions_shouldCreateTheNewSubject() throws Exception {

		// arrange
		Subject expectedSubject = new Subject("Fach4");

		// act
		MockHttpServletResponse response = mockMvc
			.perform(post("/subjects/").session(adminSession).contentType(MediaType.APPLICATION_JSON)
			.content(mapper.writeValueAsString(expectedSubject)))
			.andExpect(status().isCreated()).andReturn().getResponse();

		// assert
		Subject createdSubject = mapper.readValue(response.getContentAsString(), Subject.class);
		assertTrue("Name of new subject should be " + 
					expectedSubject.getName() + 
					" but was " + 
					createdSubject.getName(), 
			createdSubject.getName().equals(expectedSubject.getName()));
	}

	@NeedsSessions
	@NeedsTestClasses
	@Test
	public void GET_subjectsIdPools_shouldReturnArrayOfQuestionPools() throws Exception {

		// act
		String response = mockMvc
			.perform(get("/subjects/" + testClasses.subject1.getId() + "/pools").session(userSession))
			.andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

		// assert
		assertTrue("Array of questionPools should be returned but was: " + response, 
			JsonIterableChecker.representsIterableOfClass(response, QuestionPool.class));
	}

	@NeedsSessions
	@NeedsTestClasses
	@Test
	public void GET_subjectsIdPools_shouldReturnCorrectProperties() throws Exception {

		// act
		mockMvc.perform(get("/subjects/" + testClasses.subject1.getId() + "/pools").session(userSession))
			.andExpect(status().isOk())
			
			// assert
			.andExpect(jsonPath("$[0].id", is(any(Integer.class))))
			.andExpect(jsonPath("$[0].name", is(any(String.class))))
			.andExpect(jsonPath("$[0].maxScoreOfConcreteTest", is(any(Integer.class))))
			.andExpect(jsonPath("$[0].percentageToPass", is(any(Integer.class))))
			.andExpect(jsonPath("$[0].categories").doesNotExist());

	}
	
	@NeedsSessions
	@NeedsTestClasses
	@Test
	public void POST_subjectsIdPools_withWrongPermissions_shouldReturn403() throws Exception {

		// arrange
		QuestionPool newPool = new QuestionPool("newPool");
		newPool.setSubject(testClasses.subject1);
		newPool.setPercentageToPass(200);

		// act
		ResultActions result = mockMvc.perform(post("/subjects/" + testClasses.subject1.getId() + "/pools").session(userSession)
			.contentType(MediaType.APPLICATION_JSON)
			.content(mapper.writeValueAsString(newPool)));
		
		System.out.println(mapper.writeValueAsString(newPool));
				
			// assert
			result.andExpect(status().isForbidden());
	}

	@NeedsSessions
	@NeedsTestClasses
	@Test
	public void POST_subjectsIdPools_withCorrectPermissions_shouldReturnCreatedPool()
		throws Exception {

		// arrange
		QuestionPool newPool = new QuestionPool("newPool");
		newPool.setSubject(testClasses.subject1);
		newPool.setPercentageToPass(200);

		// act
		mockMvc
			.perform(post("/subjects/" + testClasses.subject1.getId() + "/pools")
			.session(adminSession).contentType(MediaType.APPLICATION_JSON)
			.content(mapper.writeValueAsString(newPool)))
			.andExpect(status().isCreated())
			
			// assert
			.andExpect(jsonPath("$.name", is(equalTo(newPool.getName()))))
			.andExpect(jsonPath("$.percentageToPass", is(equalTo(newPool.getPercentageToPass()))))
			.andExpect(jsonPath("$.id", is(any(Integer.class))));
	}

	@NeedsSessions
	@NeedsTestClasses
	@Test
	public void POST_subjectsIdPools_withCorrectPermissions_shouldCreatePool() throws Exception {

		// arrange
		String poolName = "Very new Pool!2";
		QuestionPool newPool = new QuestionPool(poolName);
		newPool.setSubject(testClasses.subject1);
		newPool.setPercentageToPass(250);
		mockMvc
			.perform(post("/subjects/" + testClasses.subject1.getId() + "/pools").session(adminSession)
			.contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(newPool)))
			.andExpect(status().isCreated());

		// act
		mockMvc
			.perform(get("/subjects/" + testClasses.subject1.getId() + "/pools").session(userSession))
			.andExpect(status().isOk())
			
			// assert
			.andExpect(jsonPath("$[?(@.name==" + poolName + ")]").exists());

	}
}
